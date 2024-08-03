package com.example.orchestrator.dto.socket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private String from;
    private String text;
}
