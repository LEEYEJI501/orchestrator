package com.example.orchestrator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeDto {
    @NotNull
    private Long userId;
}
