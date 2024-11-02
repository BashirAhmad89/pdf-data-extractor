package com.project.pdfdataextractor.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for converting PDF content to JSON format.
 * Provides methods to extract text from PDF files and structure it as JSON.
 */
@Service
public class PdfToJsonService {

    private static final Logger logger = LoggerFactory.getLogger(PdfToJsonService.class);

    /**
     * Extracts text from a PDF file.
     *
     * @param inputStream The input stream of the PDF file.
     * @return The extracted text content of the PDF.
     * @throws IOException if an error occurs during PDF processing.
     */
    public String convertPdfToText(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            logger.error("Error processing the PDF file");
            throw new RuntimeException("Error processing the PDF file", e);
        }
    }

    /**
     * Converts text content into a JSON-like structure.
     *
     * @param text The text to convert to JSON format.
     * @return A map containing the JSON structure with the text content.
     */
    public Map<String, Object> convertTextToJson(String text) {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("content", text);
        return jsonData;
    }
}