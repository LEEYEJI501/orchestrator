package com.example.orchestrator.dto.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutputMessageDto {
    private String from;
    private String text;

    public OutputMessageDto(String from, String text) {
        this.from = from;
        this.text = text;
    }
}
