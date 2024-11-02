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

@Service
public class PdfToJsonService {

    private static final Logger logger = LoggerFactory.getLogger(PdfToJsonService.class);

    public String convertPdfToText(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            logger.error("Fehler beim Verarbeiten der PDF-Datei");
            throw new RuntimeException("Fehler beim Verarbeiten der PDF-Datei", e);
        }
    }

    public Map<String, Object> convertTextToJson(String text) {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("content", text);
        return jsonData;
    }
}