package com.captureimage.extractimage.process;

import com.captureimage.extractimage.dto.ImagePropertyDTO;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OutputStreamDocument extends OutputStream {


    private List<BufferedImage> images;
    @Setter
    private List<ImagePropertyDTO> imagePropertyDTOS;

    @Override
    public void write(int b) throws IOException {
        new ByteArrayOutputStream(b);
    }

    public void write(byte[] body, File file) throws IOException {

        Path path = Files.write(file.toPath(), body);
        PDFEngine engine = new PDFEngine(PDDocument.load(path.toFile()));
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
