package com.captureimage.extractimage.services;

import com.captureimage.extractimage.process.PDFEngine;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class PDFConverter {

    public static List<BufferedImage> docxToPDF(byte[] bytes) {

        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                XWPFDocument document = new XWPFDocument(inputStream);
        ) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
            inputStream.transferTo(outputStream);

            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, outputStream);
            pdfDocument.open();

            document.getParagraphs().forEach(paragraph -> {
                try {
                    pdfDocument.add(new Paragraph(paragraph.getText()));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });

            pdfDocument.close();

            PDFEngine engine = new PDFEngine(PDDocument.load(new ByteArrayInputStream(outputStream.toByteArray())));
            System.out.println("DOCX convertido para PDF com sucesso!");
            return engine.getImages();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
