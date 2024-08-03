package com.example.orchestrator.dto;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequestDto {
    @Min(value = 0, message = "Page number cannot be less than 0")
    private int page;

    @Min(value = 1, message = "Page size must be at least 1")
    private int pageSize;
}
