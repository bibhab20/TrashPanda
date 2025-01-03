package org.aai.atc.crawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecursiveCrawler implements Crawler {

    @Override
    public List<File> crawl(String rootPath) throws IOException {
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
        if (files != null) {
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
}
