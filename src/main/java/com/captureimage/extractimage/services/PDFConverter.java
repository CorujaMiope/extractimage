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

    private static Path path = Path.of("C:\\Users\\ADMIN\\TEMP_PDF");
    private static File temp;

    static {
        try {
            temp = File.createTempFile("temp", ".pdf", path.toFile());
            //TODO criar uma função chamada "private String getPath()" que ira retornar todo esse escopo do path e file como caminho
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ImagePropertyDTO> docxToPDF(File file) throws IOException {

        Document doc = new Document(file.getAbsolutePath());

        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);

        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }

        doc.saveToFile(temp.getAbsolutePath(), parameter);

        PDFEngine engine = new PDFEngine(PDDocument.load(temp));

        return engine.getImagePropertyDTOs();
    }

    public static List<ImagePropertyDTO> docxToPDF(byte[] bytes) throws IOException {

        Document doc = new Document(new ByteArrayInputStream(bytes));
        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);

        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }

        doc.saveToFile(temp.getAbsolutePath(), parameter);

        PDFEngine engine = new PDFEngine(PDDocument.load(temp));
        return engine.getImagePropertyDTOs();
    }
}
