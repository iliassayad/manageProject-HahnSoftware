package net.ayad.manageprojectbackend.service;

import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.Task;
import net.ayad.manageprojectbackend.entity.TaskStatus;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.ProjectNotFoundException;
import net.ayad.manageprojectbackend.mapper.ProjectMapper;
import net.ayad.manageprojectbackend.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ProjectService projectService;


    private User user;
    private Project project;
    private Task task;
    private ProjectResponseDTO projectResponseDTO;
    private UpdateProjectDTO updateProjectDTO;
    private CreateProjectDTO createProjectDTO;

    @BeforeEach
    void setup() {

        user = User.builder()
                .id(1L)
                .email("mail@exemple.com")
                .password("password1234")
                .firstName("John")
                .lastName("Doe")
                .build();

        project = Project.builder()
                .id(1L)
                .title("project 1")
                .description("project 1 description")
                .owner(user)
                .createdAt(LocalDateTime.now())
                .build();

        task = Task.builder()
                .id(1L)
                .title("Task 1")
                .description("Task 1 description")
                .status(TaskStatus.TODO)
                .project(project)
                .createdAt(LocalDateTime.now())
                .build();

        project.setTasks(List.of(task));

        projectResponseDTO = new ProjectResponseDTO(
                1L,
                "project 1",
                "project 1 Description",
                LocalDateTime.now(),
                1,
                0,
                0
        );

        createProjectDTO = new CreateProjectDTO(
                "project 1",
                "Project to create"
        );

        updateProjectDTO = new UpdateProjectDTO(
                "project to update",
                "description update"
        );
    }


    @DisplayName("Get All Projects Tests")
    @Nested
    class GetAllProject {

        @Test
        void whenUserIsAuthenticated_shouldReturnListOfProjects() {

            //Arrange
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findAllByOwnerId(user.getId())).thenReturn(List.of(project));
            when(projectMapper.toProjectResponseDTO(any(Project.class))).thenReturn(projectResponseDTO);

            //Act
            List<ProjectResponseDTO> projects = projectService.getAllProjects();

            //Assert
            assertEquals(1, projects.size());

        }

    }

    @DisplayName("Get Project By ID Tests")
    @Nested
    class GetProjectById {

        @Test
         void whenProjectWithProvidedIdExist_shouldReturnOneProjectResponseDTO() {
             //Arrange
             Long id = 1L;
             when(authService.getCurrentUser()).thenReturn(user);
             when(projectRepository.findByIdAndOwnerId(id, authService.getCurrentUser().getId())).thenReturn(Optional.of(project));
             when(projectMapper.toProjectResponseDTO(project)).thenReturn(projectResponseDTO);

             //Act
             ProjectResponseDTO projectResponseDTO1 = projectService.getProjectById(id);

             //Assert
             assertNotNull(projectResponseDTO1);
             verify(projectRepository).findByIdAndOwnerId(any(Long.class), any(Long.class));
             verify(projectMapper).toProjectResponseDTO(any(Project.class));

         }

         @Test
         void whenProjectWithProvidedIdDoesNotExist_shouldThrowProjectNotFoundException() {
            //Arrange
             Long id = 2L;
             when(authService.getCurrentUser()).thenReturn(user);
             when(projectRepository.findByIdAndOwnerId(id, user.getId())).thenReturn(Optional.empty());


             //Act & Assert
             ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(id));

             //Assert
             verifyNoInteractions(projectMapper);
             assertEquals("project not found with id: " + id, exception.getMessage());
         }

    }

    @DisplayName("Create Project Tests")
    @Nested
    class CreateProject {

        @Test
        void whenValidInputIsProvided_shouldReturnProjectResponseDTO() {
            //Arrange
            Long id = 1L;
            when(projectMapper.toProject(createProjectDTO)).thenReturn(project);
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.save(project)).thenReturn(project);
            when( projectMapper.toProjectResponseDTO(project)).thenReturn(projectResponseDTO);

            //Act
            ProjectResponseDTO projectResponseDTO1 = projectService.createProject(createProjectDTO);


            //Assert
            verify(projectMapper).toProject(createProjectDTO);
            verify(projectRepository).save(project);
            verify(projectMapper).toProjectResponseDTO(project);
            verify(projectRepository).save(argThat(project1 -> project1.getOwner().equals(user)));
        }


    }

    @DisplayName("Update Project Tests")
    @Nested
    class UpdateProject {

        @Test
        void whenValidInputIsProvided_shouldReturnProjectResponseDTO() {
            //Arrange
            Long id = 1L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(id, user.getId())).thenReturn(Optional.of(project));

            when(projectMapper.updateProjectFromDTO(updateProjectDTO, project)).thenReturn(project);
            when(projectRepository.save(project)).thenReturn(project);
            when(projectMapper.toProjectResponseDTO(project)).thenReturn(projectResponseDTO);


            //Act
            ProjectResponseDTO responseDTO = projectService.updateProject(id, updateProjectDTO);


            //Assert
            verify(projectRepository).findByIdAndOwnerId(id, user.getId());
            verify(projectMapper).updateProjectFromDTO(updateProjectDTO, project);
            verify(projectRepository).save(project);
            verify(projectMapper).toProjectResponseDTO(project);
        }


        @Test
        void whenProjectDoesNotExist_shouldThrowProjectNotFoundException() {
            //Arrange
            Long id = 2L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(id, user.getId())).thenReturn(Optional.empty());


            //Act
            assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject(id, updateProjectDTO));


            //Assert
            verifyNoInteractions(projectMapper);
            verify(projectRepository, never()).save(any(Project.class));
        }

    }

    @DisplayName("Delete Project Test")
    @Nested
    class DeleteProject {


        @Test
        void whenProjectExists_shouldDeleteProject() {

            //Arrange
            Long projectId = 1L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(projectId, user.getId())).thenReturn(Optional.of(project));

            //Act
            projectService.deleteProject(projectId);

            //Assert
            verify(projectRepository).delete(project);

        }

        @Test
        void whenProjectDoesNotExist_shouldThrowProjectNotFoundException() {

            //Arrange
            Long id = 2L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(id, user.getId())).thenReturn(Optional.empty());


            //Act
            ProjectNotFoundException projectNotFoundException = assertThrows(ProjectNotFoundException.class, () ->
                            projectService.deleteProject(id)
            );


            //Assert
            verify(projectRepository, never()).deleteById(any(Long.class));
        }

    }


}