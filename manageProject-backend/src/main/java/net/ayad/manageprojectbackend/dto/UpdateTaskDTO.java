package net.ayad.manageprojectbackend.dto;

import jakarta.validation.constraints.NotBlank;
import net.ayad.manageprojectbackend.entity.TaskStatus;

public record UpdateTaskDTO(

        @NotBlank(message = "Title must not be blank")
        String title,
        String description,

        @NotBlank(message = "Status must not be blank")
        TaskStatus status
) {
}
