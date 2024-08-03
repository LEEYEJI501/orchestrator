package com.example.orchestrator.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateReplyDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Content is required")
    private String content;
}
