package net.ayad.manageprojectbackend.dto;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
