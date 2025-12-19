package net.ayad.manageprojectbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password is required")
        String password
) {
}
