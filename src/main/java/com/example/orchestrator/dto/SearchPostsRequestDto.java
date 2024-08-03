package com.example.orchestrator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SearchPostsRequestDto extends PaginationRequestDto {
    @NotNull(message = "Search keyword is required")
    private String keyword;
}