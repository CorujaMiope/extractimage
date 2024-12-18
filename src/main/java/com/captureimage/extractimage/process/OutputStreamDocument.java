package com.captureimage.extractimage.process;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OutputStreamDocument extends OutputStream {


    private List<BufferedImage> images;

    @Override
    public void write(int b) throws IOException {
        new ByteArrayOutputStream(b);
    }

    public void write(String text) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(text.getBytes().length);
        stream.write(text.getBytes(StandardCharsets.UTF_8));
        stream.writeBytes(text.getBytes(StandardCharsets.UTF_8));stream.write(text.getBytes(StandardCharsets.UTF_8));
        stream.close();
        String url = "https://www.caesp.com.br/libwww/colegios/uploads/uploadsMateriais/07022023143038A-Historia-do-Mundo-Para-Quem-Tem-Pressa-by-Emma-Marriott-z-lib.org_.epub_.pdf";
    }

    private void setImages(List<BufferedImage> images) {
        this.images = images;
    }

    public List<BufferedImage> getImages() {
        return images;
    }
}
