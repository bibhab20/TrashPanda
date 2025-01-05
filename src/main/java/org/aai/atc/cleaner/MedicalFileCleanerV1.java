package org.aai.atc.cleaner;

import lombok.extern.slf4j.Slf4j;
import org.aai.atc.config.AppConfig;
import org.aai.atc.crawler.Crawler;
import org.aai.atc.exception.ExtractionValidationException;
import org.aai.atc.extractor.Extractor;
import org.aai.atc.filter.FileFilter;
import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class MedicalFileCleanerV1 implements FolderCleaner{
    private static final String DESTINATION_BASE_PATH_KEY = "cleaner.medical.destinationBasePath";
    Crawler crawler;
    FileFilter pdfFilter;
    Extractor nameAndDateExtractor;
    AppConfig appConfig;

    public MedicalFileCleanerV1(Crawler crawler, FileFilter pdfFilter, Extractor nameAndDateExtractor, AppConfig appConfig) {
        this.crawler = crawler;
        this.pdfFilter = pdfFilter;
        this.nameAndDateExtractor = nameAndDateExtractor;
        this.appConfig = appConfig;
    }

    @Override
    public void cleanFolder(String folderPath) throws Exception {
        //crawl the files
        List<File> allFiles = crawler.crawl(folderPath);

        //filter out the pdfs, else throw an exception
        List<File> pdfFiles = new ArrayList<File>();
        for (File file : allFiles) {
            if (pdfFilter.accept(file)) {
                pdfFiles.add(file);
                log.info("found pdf file: {}", file.getName());
            }
        }

        //extract the name and issue date from the pdf and move the file
        for (File file : pdfFiles) {
            Map<MetaDataKey, Object> nameAndDate = new HashMap<MetaDataKey, Object>();
            try {
                nameAndDate= nameAndDateExtractor.extract(file);
                String employeeName = (String) nameAndDate.get(MetaDataKey.EMPLOYEE_NAME);
                String employeeId = (String) nameAndDate.get(MetaDataKey.EMPLOYEE_ID);
                Date date = (Date) nameAndDate.get(MetaDataKey.VALID_UNTIL_DATE);
                moveFile(file, employeeName, employeeId, date);
            } catch (ExtractionValidationException e) {
                log.warn("Not a medical assessment file, file name {}, error {}", file.getName(), e.getMessage());
            }
        }


    }

    private String getDestinationPath(String employeeName, String employeeId, Date validUtildate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String dateString =  dateFormat.format(validUtildate);
        String employeeNameString = employeeName.replace("\\s+", "-") + "_";
        String fileName = employeeNameString + "Medical_" + dateString + ".pdf";
        return appConfig.getProperties().getProperty(DESTINATION_BASE_PATH_KEY) +
                employeeNameString +
                employeeId.trim() + "/" +
                fileName;
    }

    private void moveFile(File file, String employeeName, String employeeId, Date validUntildate) {
        log.debug("moving file: {}", file.getName());
        Path sourcePath = file.toPath();
        Path destinationPath = Paths.get(getDestinationPath(employeeName, employeeId, validUntildate));
        try {
            // Ensure the target directory exists
            Files.createDirectories(destinationPath.getParent());

            // Move and rename the file
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            log.debug("moved file: {} from source {} and to destination path {}", file.getName(), sourcePath, destinationPath);
        } catch (Exception e) {
            log.error("Error moving the file {}, exception {}", file.getName(), e.getMessage());
        }
    }
}
