package net.ayad.manageprojectbackend.service;

import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.ProjectNotFoundException;
import net.ayad.manageprojectbackend.exception.UserNotFoundException;
import net.ayad.manageprojectbackend.mapper.ProjectMapper;
import net.ayad.manageprojectbackend.repository.ProjectRepository;
import net.ayad.manageprojectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthService authService;

    public List<ProjectResponseDTO> getAllProjects() {
        Long currentUserId = authService.getCurrentUser().getId();
        List<Project> projects = projectRepository.findAllByOwnerId(currentUserId);
        return projects.stream()
                .map(projectMapper::toProjectResponseDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(Long id){
        Project project = projectRepository.findByIdAndOwnerId(id, authService.getCurrentUser().getId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return projectMapper.toProjectResponseDTO(project);
    }

    public ProjectResponseDTO createProject(CreateProjectDTO createProjectDTO){
        Project project = projectMapper.toProject(createProjectDTO);
        project.setOwner(authService.getCurrentUser());
        Project savedProject = projectRepository.save(project);
        return projectMapper.toProjectResponseDTO(savedProject);
    }

    public ProjectResponseDTO updateProject(Long id, UpdateProjectDTO updateProjectDTO){
        Project existingProject = projectRepository.findByIdAndOwnerId(id, authService.getCurrentUser().getId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
        Project updatedProject = projectMapper.updateProjectFromDTO(updateProjectDTO, existingProject);
        Project savedProject = projectRepository.save(updatedProject);
        return projectMapper.toProjectResponseDTO(savedProject);
    }

    public void deleteProject(Long id){
        Project existingProject = projectRepository.findByIdAndOwnerId(id, authService.getCurrentUser().getId())
                .orElseThrow(() -> new ProjectNotFoundException(id));
        projectRepository.delete(existingProject);
    }


//    private User getCurrentUser() {
//        Long userId = authService.getCurrentUserId();
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException(userId));
//    }
}
