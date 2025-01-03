package org.aai.atc.filter;

import java.io.File;

public interface FileFilter {
    public boolean accept(File file);
}
