package org.aai.atc.crawler;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RecursiveCrawler implements Crawler {

    @Override
    public List<File> crawl(String rootPath) throws IOException {
        log.info("starting crawl in folder {}", rootPath);
        List<File> files = new ArrayList<>();
        File rootDirectory = new File(rootPath);
        if (rootDirectory.exists() && rootDirectory.isDirectory()) {
            addFiles(rootDirectory, files);
        } else {
            throw new IOException(rootPath + " is not a directory");
        }
        return files;
    }

    private void addFiles(File dir, List<File> output) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            log.warn("No files found in {}", dir.getAbsolutePath());
            return;
        }
        log.info("found {} files in directory {}", files.length, dir.getAbsolutePath());
        for (File file : files) {
            if (file.isFile()) {
                output.add(file);
            } else if (file.isDirectory()) {
                //Recursive call for subdirectories
                addFiles(file, output);
            }
        }

    }
}
