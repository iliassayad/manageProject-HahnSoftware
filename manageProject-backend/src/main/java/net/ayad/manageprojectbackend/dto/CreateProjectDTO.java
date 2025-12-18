package net.ayad.manageprojectbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectDTO(
        @NotBlank(message = "Title must not be blank")
        String title,
        String description
) {
}
