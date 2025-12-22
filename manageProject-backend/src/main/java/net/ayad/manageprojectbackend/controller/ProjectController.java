package net.ayad.manageprojectbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project Controller", description = "APIs for managing projects")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;


    @GetMapping
    @Operation(summary = "Get All Projects", description = "Retrieve a list of all projects")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of projects")
    ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Project by ID", description = "Retrieve a project by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the project")
    ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO projectResponseDTO = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Create New Project", description = "Create a new project with the provided details")
    @ApiResponse(responseCode = "201", description = "Successfully created the project")
    ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        ProjectResponseDTO projectResponseDTO = projectService.createProject(createProjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Project", description = "Update an existing project by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the project")
    ResponseEntity<ProjectResponseDTO> updateProject(@Valid @PathVariable Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        ProjectResponseDTO projectResponseDTO = projectService.updateProject(id, updateProjectDTO);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Project", description = "Delete a project by its ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the project")
    ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
