package net.ayad.manageprojectbackend.dto;


import java.time.LocalDateTime;

public record ProjectResponseDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        int totalTasks,
        int completedTasks,
        double completionPercentage
) {
}
