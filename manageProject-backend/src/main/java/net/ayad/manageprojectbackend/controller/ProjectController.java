package net.ayad.manageprojectbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.status(HttpStatus.FOUND).body(projects);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO projectResponseDTO = projectService.getProjectById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(projectResponseDTO);
    }

    @PostMapping
    ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectDTO createProjectDTO) {
        ProjectResponseDTO projectResponseDTO = projectService.createProject(createProjectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectResponseDTO);
    }

    @PutMapping("/{id}")
    ResponseEntity<ProjectResponseDTO> updateProject(@Valid @PathVariable Long id, @RequestBody UpdateProjectDTO updateProjectDTO) {
        ProjectResponseDTO projectResponseDTO = projectService.updateProject(id, updateProjectDTO);
        return ResponseEntity.status(HttpStatus.OK).body(projectResponseDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
