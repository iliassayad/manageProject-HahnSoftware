package net.ayad.manageprojectbackend.dto;

import net.ayad.manageprojectbackend.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt,
        Long projectId
) {
}
