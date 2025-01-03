package org.aai.atc.extractor;

import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.util.Map;

public interface Extractor {

    public Map<MetaDataKey, Object> extract(File file);
}
