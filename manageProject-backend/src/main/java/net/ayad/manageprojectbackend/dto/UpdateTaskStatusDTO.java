package net.ayad.manageprojectbackend.dto;

import jakarta.validation.constraints.NotBlank;
import net.ayad.manageprojectbackend.entity.TaskStatus;

public record UpdateTaskStatusDTO(
        @NotBlank(message = "Status must not be blank")
        TaskStatus status
) {
}
