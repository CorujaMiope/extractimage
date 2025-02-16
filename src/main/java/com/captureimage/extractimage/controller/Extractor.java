package com.captureimage.extractimage.controller;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.process.OutputStreamDocument;
import com.captureimage.extractimage.process.PDFEngine;
import com.captureimage.extractimage.records.FileRecord;
import com.captureimage.extractimage.services.PDFConverter;
import enums.DOC_TYPE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


//@RestController
//@RequestMapping("/archive/extract")
public class Extractor {

    private static final Logger log = LogManager.getLogger(Extractor.class);
    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<byte[]> response;

    //    @GetMapping("/foundimage")
    public List<ImagePropertyDTO> inspectType(@RequestBody FileRecord file) throws IOException {

        try {

            if (UrlUtils.isAbsoluteUrl(file.path())) {
                return getArchiveFromWeb(file.path());
            }

            File archive = new File(file.path());


            String type = FileUtils.getExtension(file.path()).toUpperCase();

            switch (type) {
                case "PDF" -> {
                    return load(archive);
                }
                case "DOCX" -> {
                    return convertToPDF(archive);

                }
                default -> {
                    return null;
                }
            }

//            PDDocument doc = new  PDDocument();
//            doc.save(new File(file.path()));
//            PDFEngine engine = new PDFEngine(doc);
//            return engine.getImagePropertyDTOs();

        } catch (Exception ex) {
            log.error("\n- The action could not be performed because: ", ex);
        }
        return List.of();
    }

    private List<ImagePropertyDTO> convertToPDF(File file) throws IOException {
        List<ImagePropertyDTO> dtos = PDFConverter.docxToPDF(file);
        return dtos;
    }

    private List<ImagePropertyDTO> load(File file) throws IOException {
        PDDocument doc = PDDocument.load(file);
        PDFEngine engine = new PDFEngine(doc);
        return engine.getImagePropertyDTOs();
    }

    private List<ImagePropertyDTO> load(PDDocument doc) throws IOException {
        PDFEngine engine = new PDFEngine(doc);
        return engine.getImagePropertyDTOs();
    }


    private List<ImagePropertyDTO> getArchiveFromWeb(String path) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder(URI.create(path)).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        List<ImagePropertyDTO> dtoList = new ArrayList<>();

        if (response.statusCode() == 200) {


            DOC_TYPE type = verifyTypeDoc(response.body());

            switch (type) {
                case PDF -> {
                    OutputStreamDocument outputStreamDocument = new OutputStreamDocument();
                    outputStreamDocument.write(response.body());
                    dtoList = outputStreamDocument.getImagePropertyDTOS();
                }
                case DOCX -> {
                    dtoList = PDFConverter.docxToPDF(response.body());
                }
            }


        }

        return dtoList;
    }

    private DOC_TYPE verifyTypeDoc(byte[] body) {

        if (body[0] == (byte) 0x25 && body[1] == (byte) 0x50 && body[2] == (byte) 0x44 && body[3] == (byte) 0x46) {
            return DOC_TYPE.PDF;

        } else if (body[0] == (byte) 0x50 && body[1] == (byte) 0x4B && body[2] == (byte) 0x03 && body[3] == (byte) 0x04) {
            return DOC_TYPE.DOCX;

        } else if (body[0] == (byte) 0xD0 && body[1] == (byte) 0xCF && body[2] == (byte) 0x11 && body[3] == (byte) 0xE0 && body[4] == (byte) 0xA1 && body[5] == (byte) 0xB1 && body[6] == (byte) 0x1A && body[7] == (byte) 0xE1) {
            return DOC_TYPE.DOC;

        } else {
            return DOC_TYPE.DEFAULT;
        }
    }
}
