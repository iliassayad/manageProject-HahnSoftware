package net.ayad.manageprojectbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Task Controller", description = "APIs for managing tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{projectId}")
    @Operation(summary = "Get All Tasks by Project ID", description = "Retrieve a list of all tasks for a specific project")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@PathVariable Long projectId) {
        List<TaskResponseDTO> tasks = taskService.getAllTasksByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get Task by ID", description = "Retrieve a task by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the task")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    @PostMapping
    @Operation(summary = "Create New Task", description = "Create a new task with the provided details")
    @ApiResponse(responseCode = "201", description = "Successfully created the task")
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody CreateTaskDTO createTaskDTO) {
        TaskResponseDTO createdTask = taskService.createTask(createTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update Task", description = "Update an existing task by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the task")
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
    @Operation(summary = "Update Task Status", description = "Update the status of an existing task by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the task status")
    public ResponseEntity<TaskResponseDTO> updateTaskStatus(@Valid @PathVariable Long taskId, @RequestBody UpdateTaskStatusDTO status) {
        TaskResponseDTO updatedTask = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }
}
