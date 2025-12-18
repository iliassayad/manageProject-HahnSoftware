package net.ayad.manageprojectbackend.exception;

public class ProjectNotFoundException extends RuntimeException {
  public ProjectNotFoundException(Long id) {
    super("project not found with id: " + id);
  }
}
