package com.captureimage.extractimage.services;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.process.PDFEngine;
import com.spire.doc.Document;
import com.spire.doc.PdfConformanceLevel;
import com.spire.doc.ToPdfParameterList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class PDFConverter {

    private static final Logger log = LogManager.getLogger(PDFConverter.class);
    private static ToPdfParameterList parameter = new ToPdfParameterList();

    public static List<ImagePropertyDTO> docxToPDF(File file) throws IOException {

        Document doc = new Document(file.getAbsolutePath());

        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);

        File temp = getFile();

        if (temp != null) {
            doc.saveToFile(temp.getAbsolutePath(), parameter);
            PDFEngine engine = new PDFEngine(PDDocument.load(temp));
            return engine.getImagePropertyDTOs();
        }

        return List.of();
    }

    private static File getFile() {
        //TODO criar uma função chamada "private String getPath()" que ira retornar todo esse escopo do path e file como caminho

        Path path = Path.of("C:\\TEMP_PDF");

        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }

        try {
            File temp = File.createTempFile("temp", ".pdf", path.toFile());
            return temp;

        } catch (IOException e) {
            log.error("Error because: ", e);
        }

        return null;
    }

    public static List<ImagePropertyDTO> docxToPDF(byte[] bytes) throws IOException {

        Document doc = new Document(new ByteArrayInputStream(bytes));
        parameter.setPdfConformanceLevel(PdfConformanceLevel.Pdf_A_1_A);

        File temp = getFile();

        if (temp != null) {
            doc.saveToFile(temp.getAbsolutePath(), parameter);
            PDFEngine engine = new PDFEngine(PDDocument.load(temp));
            return engine.getImagePropertyDTOs();
        }

        return List.of();
    }
}
