package com.captureimage.extractimage.process;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OutputStreamDocument extends OutputStream {


    private List<BufferedImage> images;

    @Override
    public void write(int b) throws IOException {
        new ByteArrayOutputStream(b);
    }

    public void write(String text, File file) throws IOException {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream(text.getBytes().length);
//        stream.write(text.getBytes());
//        stream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(text.getBytes());
        Path path = Files.write(file.toPath(), inputStream.readAllBytes());
        inputStream.close();

    }

    private void setImages(List<BufferedImage> images) {
        this.images = images;
    }
    public List<BufferedImage> getImages() {
        return images;
    }
}
