package net.ayad.manageprojectbackend.service;

import net.ayad.manageprojectbackend.dto.CreateTaskDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskStatusDTO;
import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.Task;
import net.ayad.manageprojectbackend.entity.TaskStatus;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.ProjectNotFoundException;
import net.ayad.manageprojectbackend.exception.TaskNotFoundException;
import net.ayad.manageprojectbackend.mapper.TaskMapper;
import net.ayad.manageprojectbackend.repository.ProjectRepository;
import net.ayad.manageprojectbackend.repository.TaskRepository;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private TaskService taskService;

    private CreateTaskDTO createTaskDTO;
    private Project project;
    private Task task;
    private Task task2;
    private User user;
    private TaskResponseDTO taskResponseDTO;
    private UpdateTaskDTO updateTaskDTO;
    private UpdateTaskStatusDTO updateTaskStatusDTO;

    @BeforeEach
    void setup() {

        user = User.builder()
                .id(1L)
                .email("iliass@gmail.com")
                .password("password")
                .firstName("Iliass")
                .lastName("Ayad")
                .build();

        project = Project.builder()
                .id(1L)
                .title("Test Project")
                .description("This is a test project")
                .createdAt(LocalDateTime.now())
                .owner(user)
                .build();

        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("This is a test task")
                .status(TaskStatus.TODO)
                .createdAt(LocalDateTime.now())
                .project(project)
                .build();

        task2 = Task.builder()
                .id(2L)
                .title("Second Task")
                .description("This is the second test task")
                .status(TaskStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now())
                .project(project)
                .build();

        project.setTasks(List.of(task, task2));

        createTaskDTO = new CreateTaskDTO(
                "Test Task",
                "This is a test task",
                1L
        );

        updateTaskStatusDTO = new UpdateTaskStatusDTO(
                TaskStatus.COMPLETED
        );




        taskResponseDTO = new TaskResponseDTO(
                1L,
                "Test Task",
                "This is a test task",
                TaskStatus.TODO,
                LocalDateTime.now(),
                1L
        );

        updateTaskDTO = new UpdateTaskDTO(
                "Updated Task Title",
                "Updated Task Description",
                TaskStatus.COMPLETED
        );





    }


    @DisplayName("Get All Tasks By Project Id Test")
    @Nested
    class GetAllTasksByProjectId{

        @DisplayName("When valid project id is provided, should return list of tasks")
        @Test
        void whenValidProjectId_shouldReturnListOfTasks(){
            //Arrange
            Long projectId = 1L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(projectId, authService.getCurrentUser().getId()))
                    .thenReturn(Optional.of(project));
            when(taskRepository.findAllByProjectId(project.getId()))
                    .thenReturn(List.of(task, task2));
            when(taskMapper.toTaskResponseDTO(any(Task.class)))
                    .thenReturn(taskResponseDTO);


            //Act
           List<TaskResponseDTO> tasks = taskService.getAllTasksByProjectId(projectId);

            //Assert
            assertEquals(2, tasks.size());
            assertEquals(taskResponseDTO, tasks.getFirst());
            assertEquals(taskResponseDTO, tasks.getLast());
        }

        @DisplayName("When project not found, should throw ProjectNotFoundException")
        @Test
        void whenProjectNotFound_shouldThrowProjectNotFoundException(){
            //Arrange
            Long projectId = 1L;
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(projectId, authService.getCurrentUser().getId()))
                    .thenReturn(Optional.empty());

            //Act & Assert
            ProjectNotFoundException projectNotFoundException = assertThrows(ProjectNotFoundException.class, () ->{
                taskService.getAllTasksByProjectId(projectId);
            });

            //Assert
            assertEquals("project not found with id: "+projectId, projectNotFoundException.getMessage());
            verifyNoInteractions(taskRepository, taskMapper);
        }

    }

    @DisplayName("Get Task By Id Tests")
    @Nested
    class GetTaskById {


        @Test
        void whenTaskIdIsValid_shouldReturnTask(){
            //Arrange
            Long taskId = 1L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(authService.getCurrentUser().getId(), task.getProject().getId()))
                    .thenReturn(Optional.of(project));
            when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

            //Act
            TaskResponseDTO taskResponseDTO1 = taskService.getTaskById(taskId);


            //Assert
            assertNotNull(taskResponseDTO1);
            assertEquals(taskResponseDTO, taskResponseDTO1);
            verify(taskRepository).findById(taskId);
            verify(projectRepository).findByIdAndOwnerId(
                    task.getProject().getId(),
                    authService.getCurrentUser().getId()
            );
            verify(taskMapper).toTaskResponseDTO(
                    task
            );
        }

        @Test
        void whenTaskNotFound_shouldThrowTaskNotFoundException(){
            //Arrange
            Long taskId = 2L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            //Act & Assert
            TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));

            //Assert
            assertEquals("Task not found with id: " + taskId, exception.getMessage());
        }
    }

    @DisplayName("Create Task Tests")
    @Nested
    class CreateTask {

        @DisplayName("When valid input is provided, should create task successfully")
        @Test
        void whenValidInput_ShouldCreateTask() {
            // Arrange
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(createTaskDTO.projectId(), authService.getCurrentUser().getId()))
                    .thenReturn(Optional.of(project));
            when(taskMapper.toTask(createTaskDTO)).thenReturn(task);
            when(taskRepository.save(task)).thenReturn(task);
            when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

            // Act
            taskResponseDTO = taskService.createTask(createTaskDTO);

            // Assert
            assertNotNull(taskResponseDTO);
            assertEquals(createTaskDTO.title(), taskResponseDTO.title());
            verify(taskRepository).save(task);
            verify(taskRepository).save(argThat(task -> {
                return task.getProject().equals(project);
            }));
        }

        @Test
        void whenProjectNotFound_ShouldThrowProjectNotFoundException() {
            //Arrange
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(createTaskDTO.projectId(), authService.getCurrentUser().getId()))
                    .thenReturn(Optional.empty());

            //Act & Assert
            ProjectNotFoundException projectNotFoundException = assertThrows(ProjectNotFoundException.class, () ->{
                taskService.createTask(createTaskDTO);
            });


            //Assert
            assertEquals("project not found with id: "+createTaskDTO.projectId(), projectNotFoundException.getMessage());
            verifyNoInteractions(taskMapper, taskRepository);


        }
    }

    @DisplayName("Update Task Tests")
    @Nested
    class UpdateTask{

        @DisplayName("When valid input is provided, should update task successfully")
        @Test
        void whenValidInput_ShouldUpdateTask(){
            //Arrange
            Long taskId = 1L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(
                    task.getProject().getId(),
                    authService.getCurrentUser().getId()
            )).thenReturn(Optional.of(project));
            Task updatedTask = Task.builder()
                    .id(task.getId())
                    .title(updateTaskDTO.title())
                    .description(updateTaskDTO.description())
                    .status(updateTaskDTO.status())
                    .createdAt(task.getCreatedAt())
                    .project(task.getProject())
                    .build();
            when(taskMapper.updateTaskFromDTO(updateTaskDTO, task)).thenReturn(updatedTask);
            when(taskRepository.save(updatedTask)).thenReturn(updatedTask);
            when(taskMapper.toTaskResponseDTO(updatedTask)).thenReturn(taskResponseDTO);



            //Act
            TaskResponseDTO result = taskService.updateTask(taskId, updateTaskDTO);

            //Assert
            assertNotNull(result);
            assertEquals(taskResponseDTO, result);
            verify(taskRepository).save(updatedTask);


        }

        @DisplayName("When task not found, should throw TaskNotFoundException")
        @Test
        void whenTaskNotFound_ShouldThrowTaskNotFoundException() {
            //Arrange
            Long taskId = 2L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
            //Act & Assert
            TaskNotFoundException taskNotFoundException = assertThrows(TaskNotFoundException.class, () -> {
                taskService.updateTask(taskId, updateTaskDTO);
            });
            //Assert
            assertEquals("Task not found with id: " + taskId, taskNotFoundException.getMessage());
            verifyNoInteractions(projectRepository, taskMapper);
        }

    }

    @DisplayName("Delete Task Tests")
    @Nested
    class DeleteTask {

        @DisplayName("When valid task id is provided, should delete task successfully")
        @Test
        void whenValidTaskId_ShouldDeleteTask(){
            //Arrange
            Long taskId = 1L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(
                    task.getProject().getId(),
                    authService.getCurrentUser().getId()
            )).thenReturn(Optional.of(project));

            //Act
            taskService.deleteTask(taskId);

            //Assert
            verify(taskRepository).delete(task);
        }

        @DisplayName("When task not found, should throw TaskNotFoundException")
        @Test
        void whenTaskNotFound_ShouldThrowTaskNotFoundException() {
            //Arrange
            Long taskId = 2L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
            //Act & Assert
            TaskNotFoundException taskNotFoundException = assertThrows(TaskNotFoundException.class, () -> {
                taskService.deleteTask(taskId);
            });
            //Assert
            assertEquals("Task not found with id: " + taskId, taskNotFoundException.getMessage());
            verifyNoInteractions(projectRepository);
        }

    }

    @DisplayName("Update Task Status Tests")
    @Nested
    class UpdateTaskStatus {

        @Test
        void whenValidInput_ShouldUpdateTaskStatus(){
            //Arrange
            Long taskId = 1L;
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
            when(authService.getCurrentUser()).thenReturn(user);
            when(projectRepository.findByIdAndOwnerId(task.getProject().getId(), authService.getCurrentUser().getId()))
                    .thenReturn(Optional.of(project));
            when(taskRepository.save(any(Task.class))).thenReturn(task);
            when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);
            //Act
            TaskResponseDTO task = taskService.updateTaskStatus(taskId, updateTaskStatusDTO);
            //Assert
            verify(taskRepository).save(argThat(task17 -> task17.getStatus().equals(updateTaskStatusDTO.status())));

        }
    }







}