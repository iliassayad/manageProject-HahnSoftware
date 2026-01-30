package net.ayad.manageprojectbackend.service;

import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateTaskDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskStatusDTO;
import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.Task;
import net.ayad.manageprojectbackend.exception.ProjectNotFoundException;
import net.ayad.manageprojectbackend.exception.TaskNotFoundException;
import net.ayad.manageprojectbackend.mapper.TaskMapper;
import net.ayad.manageprojectbackend.repository.ProjectRepository;
import net.ayad.manageprojectbackend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final AuthService authService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskResponseDTO> getAllTasksByProjectId(Long projectId){
        Project project = projectRepository.findByIdAndOwnerId(
                projectId,
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<Task> tasks = taskRepository.findAllByProjectId(project.getId());
        return tasks.stream()
                .map(taskMapper::toTaskResponseDTO)
                .toList();
    }

    public TaskResponseDTO getTaskById(Long taskId){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        projectRepository.findByIdAndOwnerId(
                task.getProject().getId(),
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(task.getProject().getId()));

        return taskMapper.toTaskResponseDTO(task);
    }

    public TaskResponseDTO createTask(CreateTaskDTO createTaskDTO){
        Project project = projectRepository.findByIdAndOwnerId(
                createTaskDTO.projectId(),
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(createTaskDTO.projectId()));
        Task task = taskMapper.toTask(createTaskDTO);
        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskResponseDTO(savedTask);
    }

    public TaskResponseDTO updateTask(Long id, UpdateTaskDTO updateTaskDTO){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        projectRepository.findByIdAndOwnerId(
                existingTask.getProject().getId(),
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(existingTask.getProject().getId()));

        Task updatedTask = taskMapper.updateTaskFromDTO(updateTaskDTO, existingTask);

        Task savedTask = taskRepository.save(updatedTask);
        return taskMapper.toTaskResponseDTO(savedTask);
    }

    public void deleteTask(Long id){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        projectRepository.findByIdAndOwnerId(
                existingTask.getProject().getId(),
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(existingTask.getProject().getId()));

        taskRepository.delete(existingTask);
    }


    public TaskResponseDTO updateTaskStatus(Long id, UpdateTaskStatusDTO updateTaskStatusDTO){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        projectRepository.findByIdAndOwnerId(
                existingTask.getProject().getId(),
                authService.getCurrentUser().getId()
        ).orElseThrow(() -> new ProjectNotFoundException(existingTask.getProject().getId()));

        existingTask.setStatus(updateTaskStatusDTO.status());
        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toTaskResponseDTO(savedTask);
    }


}




