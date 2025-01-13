package com.captureimage.extractimage.process;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import lombok.Setter;


import java.awt.image.BufferedImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class OutputStreamDocument extends OutputStream {


    private List<BufferedImage> images;
    @Setter
    private List<ImagePropertyDTO> imagePropertyDTOS;

    @Override
    public void write(int b) throws IOException {
        new ByteArrayOutputStream(b);
    }

    public void write(byte[] body) throws IOException {

        System.out.println("Arquivo chegou para a extracao");
        try {
            Class<?> aClass = Class.forName("org.apache.pdfbox.pdmodel.PDDocument");
            System.out.println(aClass + "foi encontrada");
        } catch (ClassNotFoundException e) {
            System.out.println("não foi encontrada");
        }

        PDFEngine engine = new PDFEngine(PDDocument.load(body));
        System.out.println("Arquivo saiu da extracao");
        setImagePropertyDTOS(engine.getImagePropertyDTOs());
    }

    public List<ImagePropertyDTO> getImagePropertyDTOS() {
        return imagePropertyDTOS;
    }

    private void setImages(List<BufferedImage> images) {
        this.images = images;
    }

    public List<BufferedImage> getImages() {
        return images;
    }
}
