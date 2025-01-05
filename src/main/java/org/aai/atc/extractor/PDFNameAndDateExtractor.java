package org.aai.atc.extractor;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.aai.atc.exception.ExtractionValidationException;
import org.aai.atc.model.MetaDataKey;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aai.atc.util.TesseractUtil.readScannedPdf;

@Slf4j
public class PDFNameAndDateExtractor implements Extractor {
    private static final String MEDICAL_FILE_KEY_WORD = "CLASS 3 MEDICAL ASSESSMENT";
    private static final String NAME_REGEX = "Name :\\s+Mr\\s+([A-Za-z]+\\s+[A-Za-z]+)";
    private static final String EMPLOYEE_ID_REGEX = "Employee No.:(\\s+I\\d{7})";
    private static final String VALID_UNTIL_REGEX = "Valid Until :\\s+(\\d{1,2}\\s+[A-Za-z]{3}\\s+\\d{4})";

    private final Pattern namePattern = Pattern.compile(NAME_REGEX);
    private final Pattern employeeIdPattern = Pattern.compile(EMPLOYEE_ID_REGEX);
    private final Pattern validUntilPattern = Pattern.compile(VALID_UNTIL_REGEX);

    @Override
    public Map<MetaDataKey, Object> extract(File file) throws TesseractException, IOException, ExtractionValidationException {
        //reads a pdf file and extracts the employee name and date of issuance
        String extractedData = readScannedPdf(file.getPath());
        validateMedicalAssessmentFile(extractedData);
        // keys: employeeName, dateIssued
        Map<MetaDataKey, Object> map = new HashMap<MetaDataKey, Object>();
        map.put(MetaDataKey.EMPLOYEE_NAME, extractName(extractedData));
        map.put(MetaDataKey.EMPLOYEE_ID, extractEmployeeId(extractedData));
        map.put(MetaDataKey.VALID_UNTIL_DATE, extractValidUntilDate(extractedData));
        return map;
    }

    private void validateMedicalAssessmentFile(String extractedInfo) throws ExtractionValidationException {
        if (extractedInfo == null || extractedInfo.isEmpty()) {
            throw new IllegalArgumentException("Medical Assessment file is empty");
        }

        if (!extractedInfo.contains(MEDICAL_FILE_KEY_WORD)) {
            throw new ExtractionValidationException("Keyword validation failed for medical assessment file");
        }
    }

    private String extractName(String text) {
        Matcher matcher = namePattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Name validation failed for medical assessment file");
    }

    private String extractEmployeeId(String text) {
        Matcher matcher = employeeIdPattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Employee id validation failed for medical assessment file");
    }

    private Date extractValidUntilDate(String text) {
        Matcher matcher = validUntilPattern.matcher(text);
        String validUntilDateStr = "";
        if (matcher.find()) {
            validUntilDateStr = matcher.group(1);
        }

        // Parse the extracted date into a Date object
        Date validUntilDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            validUntilDate = dateFormat.parse(validUntilDateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return validUntilDate;
    }
}
