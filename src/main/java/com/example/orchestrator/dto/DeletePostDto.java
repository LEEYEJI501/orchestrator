package com.example.orchestrator.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DeletePostDto {
    @NotNull(message = "User ID is required")
    private Long userId;
}
