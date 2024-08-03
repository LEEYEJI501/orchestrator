package com.example.orchestrator.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class UpdatePostDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Title is required")
    private String title;

    @NotEmpty(message = "Content is required")
    private String content;
}
