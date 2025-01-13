package com.captureimage.extractimage.controller;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.process.OutputStreamDocument;
import com.captureimage.extractimage.process.PDFEngine;
import com.captureimage.extractimage.records.FileRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//@RestController
//@RequestMapping("/archive/extract")
public class Extractor {

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
                case "DOCX" -> convertToPDF(archive);
            }


            PDDocument doc = new  PDDocument();
            doc.save(new File(file.path()));
            PDFEngine engine = new PDFEngine(doc);
            return engine.getImagePropertyDTOs();
        } catch (Exception ex) {
            System.out.println("\n- The action could not be performed because:" + ex.getCause());
        }
        return List.of();
    }

    private void convertToPDF(File file) throws IOException {

        FileReader reader = new FileReader(file);
        Scanner scan = new Scanner(reader);

        StringBuilder photo = new StringBuilder();

        while (scan.hasNext()) {
            photo.append(scan.next());
        }

        load(file);
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
            OutputStreamDocument outputStreamDocument = new OutputStreamDocument();
            outputStreamDocument.write(response.body());
            dtoList = outputStreamDocument.getImagePropertyDTOS();
        }

        return dtoList;
    }
}
