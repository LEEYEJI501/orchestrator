package com.example.orchestrator.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovingAveragePeriodsDto {
    @NotEmpty(message = "Periods list cannot be empty")
    private List<Integer> periods;
}
