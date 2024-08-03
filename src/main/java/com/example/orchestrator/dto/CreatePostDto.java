package com.example.orchestrator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class CreatePostDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Author is required")
    private String author;

    @NotEmpty(message = "Account name is required")
    private String accountName;

    @NotEmpty(message = "Content is required")
    private String content;

    @NotNull(message = "Stock code is required")
    private Long stockCode;
}
