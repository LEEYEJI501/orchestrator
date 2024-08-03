package com.example.orchestrator.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortParamsDto {
    @Pattern(regexp = "^(name|code|transactionAmount|priceChangeRate),(asc|desc)$", message = "Invalid sort format. Use 'field,order' where order is 'asc' or 'desc'")
    private String sort = "name,asc";
}