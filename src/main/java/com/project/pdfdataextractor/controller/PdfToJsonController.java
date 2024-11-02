package com.project.pdfdataextractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling PDF-to-JSON conversion requests.
 */
@RestController
@RequestMapping("/api/pdf")
public class PdfToJsonController {

    /**
     * Converts an uploaded PDF file to JSON format.
     *
     * @param file The PDF file to be converted.
     * @return ResponseEntity containing the JSON file as a byte array with headers for download,
     *         or an error status if conversion fails.
     */
    @PostMapping(value = "/convert")
    public ResponseEntity<byte[]> convertPdfToJson(@RequestParam("file") MultipartFile file) {
        try {
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            Map<String, String> jsonContent = new HashMap<>();
            jsonContent.put("content", text);
            //test

            ObjectMapper objectMapper = new ObjectMapper();
            byte[] jsonBytes = objectMapper.writeValueAsBytes(jsonContent);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.json");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(jsonBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}