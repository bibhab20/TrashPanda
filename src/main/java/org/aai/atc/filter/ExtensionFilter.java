package org.aai.atc.filter;

import org.aai.atc.util.FileUtils;

import java.io.File;

public class ExtensionFilter implements FileFilter{
    String extension;
    public ExtensionFilter(String extension) {
        this.extension = extension;
    }


    @Override
    public boolean accept(File file) {
        return FileUtils.getExtension(file).equalsIgnoreCase(extension);
    }
}
