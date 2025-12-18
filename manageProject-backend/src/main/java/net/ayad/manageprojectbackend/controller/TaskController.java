package net.ayad.manageprojectbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateTaskDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskStatusDTO;
import net.ayad.manageprojectbackend.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{projectId}")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@PathVariable Long projectId) {
        List<TaskResponseDTO> tasks = taskService.getAllTasksByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.FOUND).body(tasks);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.status(HttpStatus.FOUND).body(task);
    }


    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        TaskResponseDTO createdTask = taskService.createTask(createTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@Valid @PathVariable Long taskId, @RequestBody UpdateTaskDTO updateTaskDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(taskId, updateTaskDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@Valid @PathVariable Long taskId, @RequestBody UpdateTaskStatusDTO status) {
        TaskResponseDTO updatedTask = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }
}
