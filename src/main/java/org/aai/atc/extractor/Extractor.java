package org.aai.atc.extractor;

import net.sourceforge.tess4j.TesseractException;
import org.aai.atc.exception.ExtractionValidationException;
import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface Extractor {

    public Map<MetaDataKey, Object> extract(File file) throws TesseractException, IOException, ExtractionValidationException;
}
