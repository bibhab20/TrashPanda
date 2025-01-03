package org.aai.atc.util;

import java.io.File;

public class FileUtils {
    public static String getExtension(File file) {
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex == -1) ? "No Extension" : fileName.substring(lastIndex + 1);
    }
}
