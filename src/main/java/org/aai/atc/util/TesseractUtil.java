package org.aai.atc.util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TesseractUtil {
    private static final String TESSERACT_DATA_PATH = "/Users/bibhabpattnayak/Development/utility/Tess4J/tessdata";

    public static String readScannedPdf(String filePath) throws IOException, TesseractException {
        // Load the PDF document
        PDDocument document = PDDocument.load(new File(filePath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // Initialize Tesseract OCR
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH); // Set Tesseract data directory
        tesseract.setLanguage("eng"); // Specify language pack (e.g., English)

        StringBuilder extractedText = new StringBuilder();

        // Process each page in the PDF
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300); // Render the page as an image
            String pageText = tesseract.doOCR(image); // Perform OCR on the image
            extractedText.append("Page ").append(page + 1).append(":\n").append(pageText).append("\n");
        }

        document.close(); // Close the PDF document
        return extractedText.toString();
    }
}
