package com.captureimage.extractimage.dto;

import lombok.Data;

@Data
public class ImagePropertyDTO {

    private Integer width;
    private Integer heigh;
    private byte[] data;

    public ImagePropertyDTO(Integer width, Integer heigh, byte[] data) {
        this.width = width;
        this.heigh = heigh;
        this.data = data;
    }
}
