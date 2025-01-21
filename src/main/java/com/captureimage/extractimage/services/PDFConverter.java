package com.captureimage.extractimage.services;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.process.PDFEngine;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.PdfConformanceLevel;
import com.spire.doc.ToPdfParameterList;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PDFConverter {

   private static ToPdfParameterList parameter = new ToPdfParameterList();
    private static Path temp;

    static {
        try {
            temp = Files.createTempFile(Path.of("src/main/resources/temp"), "temp", ".pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PDFConverter() throws IOException {
    }

    public static List<ImagePropertyDTO> docxToPDF(File file) throws IOException {

        Document doc = new Document(file.getAbsolutePath());

        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);
        doc.saveToFile(temp.toFile().getAbsolutePath(), parameter);

        PDFEngine engine = new PDFEngine(PDDocument.load(temp.toFile()));

        return engine.getImagePropertyDTOs();
    }

    public static List<ImagePropertyDTO> docxToPDF(byte[] bytes) throws IOException {

        Document doc = new Document(new ByteArrayInputStream(bytes));
        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);
        doc.saveToFile(temp.toFile().getAbsolutePath(), parameter);

        PDFEngine engine = new PDFEngine(PDDocument.load(temp.toFile()));
        return engine.getImagePropertyDTOs();
    }
}
