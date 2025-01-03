package org.aai.atc.cleaner;

import org.aai.atc.crawler.Crawler;
import org.aai.atc.extractor.Extractor;
import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MedicalFileCleanerV1 implements FolderCleaner{
    private static final String DESTINATION="";
    Crawler crawler;
    FileFilter pdfFilter;
    Extractor nameAndDateExtractor;
    @Override
    public void cleanFolder(String folderPath) throws Exception {
        //crawl the files
        List<File> allFiles = crawler.crawl(folderPath);

        //filter out the pdfs, else throw an exception
        List<File> pdfFiles = new ArrayList<File>();
        for (File file : allFiles) {
            if (pdfFilter.accept(file)) {
                pdfFiles.add(file);
            } else {
                throw new RuntimeException("Non pdf file found");
            }
        }

        //extract the name and issue date from the pdf and move the file
        for (File file : pdfFiles) {
            Map<MetaDataKey, Object> nameAndDate = nameAndDateExtractor.extract(file);
            String employeeName = (String) nameAndDate.get(MetaDataKey.EMPLOYEE_NAME);
            String date = (String) nameAndDate.get(MetaDataKey.ISSUE_DATE);
            moveFile(file, employeeName, date);
        }


    }

    private String getDestinationPath(String employeeName, String issueDate) {
        return "";
    }

    private void moveFile(File file, String employeeName, String issueDate) {
        Path sourcePath = file.toPath();
        Path destinationPath = Paths.get(getDestinationPath(employeeName, issueDate));
        try {
            // Ensure the target directory exists
            Files.createDirectories(destinationPath.getParent());

            // Move and rename the file
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File moved to: " + destinationPath);
        } catch (Exception e) {
            System.err.println("Error moving the file: " + e.getMessage());
        }
    }
}
