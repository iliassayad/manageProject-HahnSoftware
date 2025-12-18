package net.ayad.manageprojectbackend.exception;

public class EmailTakenException extends RuntimeException {
    public EmailTakenException(String email) {
        super("Email already taken: " + email);
    }
}
