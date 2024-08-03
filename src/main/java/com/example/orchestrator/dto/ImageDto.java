package com.example.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ImageDto {
    private String uploadPath;
    private String originalFilename;
    private String fileExtension;
    private long fileSize;
}
