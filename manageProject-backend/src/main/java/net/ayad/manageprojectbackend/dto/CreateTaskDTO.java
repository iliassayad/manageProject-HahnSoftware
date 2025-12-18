package net.ayad.manageprojectbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskDTO(
        @NotBlank(message = "Title must not be blank")
        String title,
        String description,

        @NotNull(message = "Project ID must not be null")
        Long projectId
) {
}
