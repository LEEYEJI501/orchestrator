package com.example.orchestrator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EmailSendDto {
    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
