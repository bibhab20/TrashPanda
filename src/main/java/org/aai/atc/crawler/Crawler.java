package org.aai.atc.crawler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Crawler {
    public List<File> crawl(String rootPath) throws IOException;
}
