package org.aai.atc;

import lombok.extern.slf4j.Slf4j;
import org.aai.atc.cleaner.MedicalFileCleanerV1;
import org.aai.atc.config.AppConfig;
import org.aai.atc.crawler.Crawler;
import org.aai.atc.crawler.RecursiveCrawler;
import org.aai.atc.extractor.Extractor;
import org.aai.atc.extractor.PDFNameAndDateExtractor;
import org.aai.atc.filter.ExtensionFilter;
import org.aai.atc.filter.FileFilter;

@Slf4j
public class TrashPandaApp {
    private static final String MEDICAL_FILE_CLEANER_PATH_KEY = "cleaner.medical.sourceFilePath";
    AppConfig appConfig;
    MedicalFileCleanerV1 medicalFileCleaner;
    Crawler crawler;
    Extractor medicalFileDataExtractor;
    FileFilter pdfFileFilter;

    public TrashPandaApp() {
        loadObjects();
    }

    private void loadObjects() {
        this.appConfig = new AppConfig("default");
        this.crawler = new RecursiveCrawler();
        this.medicalFileDataExtractor = new PDFNameAndDateExtractor();
        this.pdfFileFilter = new ExtensionFilter("pdf");
        //cleaner
        this.medicalFileCleaner = new MedicalFileCleanerV1(crawler, pdfFileFilter, medicalFileDataExtractor, appConfig);
    }

    public void run() {
        log.info("Starting TrashPandaApp");
        try {
            this.medicalFileCleaner.cleanFolder(appConfig.getProperties().getProperty(MEDICAL_FILE_CLEANER_PATH_KEY));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
