package com.captureimage.extractimage.controller;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import com.captureimage.extractimage.process.PDFEngine;
import com.captureimage.extractimage.records.FileRecord;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.ScratchFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;


@RestController
@RequestMapping("/archive/extract")
public class Extractor {

    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;

    @GetMapping("/foundimage")
    public List<ImagePropertyDTO> inspectType(@RequestBody FileRecord file) throws IOException {

        try {

            if (UrlUtils.isAbsoluteUrl(file.path())){
                return getArchiveFromWeb(file.path());
            }

            File archive = new File(file.path());

            String type = FilenameUtils.getExtension(file.path()).toUpperCase();

            switch (type) {
                case "PDF" -> {
                    return load(archive);
                }
                case "DOCX" -> convertToPDF(archive);
            }


            PDDocument doc = PDDocument.load(new File(file.path()));
            PDFEngine engine = new PDFEngine(doc);
            return engine.getImagePropertyDTOs();
        }catch (Exception ex){
            System.out.println(ex.getCause());
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


//    private List<BufferedImage> load(File file) throws IOException {
//        PDDocument doc = PDDocument.load(file);
//        PDFEngine engine = new PDFEngine(doc);
//        return engine.getImages();
//    }
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
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){

            byte[] encode = Base64.getMimeEncoder().encode(response.body().getBytes());
            ByteArrayInputStream stream = new ByteArrayInputStream(encode);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(encode);
            return load(PDDocument.load(out.toByteArray()));

        }

        return List.of();
    }
}
