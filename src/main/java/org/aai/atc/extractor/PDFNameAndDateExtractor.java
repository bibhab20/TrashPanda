package org.aai.atc.extractor;

import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PDFNameAndDateExtractor implements Extractor {
    @Override
    public Map<MetaDataKey, Object> extract(File file) {
        //TODO
        //reads a pdf file and extracts the employee name and date of issuance
        // keys: employeeName, dateIssued
        Map<MetaDataKey, Object> map = new HashMap<MetaDataKey, Object>();
        return map;
    }
}
