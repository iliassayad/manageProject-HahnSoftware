package net.ayad.manageprojectbackend.dto;

public record ApiErrorResponse(
        int statusCode,
        String message,
        long timestamp,
        String path
) {
}
