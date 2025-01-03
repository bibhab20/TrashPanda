package org.aai.atc.filter;

import org.aai.atc.util.FileUtils;

import java.io.File;

public class PDFFilter implements FileFilter {
    @Override
    public boolean accept(File file) {
        return FileUtils.getExtension(file).equals("pdf");
    }
}
