package net.ayad.manageprojectbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ayad.manageprojectbackend.dto.CreateTaskDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskStatusDTO;
import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.Task;
import net.ayad.manageprojectbackend.entity.TaskStatus;
import net.ayad.manageprojectbackend.service.CustomUserDetailsService;
import net.ayad.manageprojectbackend.service.TaskService;
import net.ayad.manageprojectbackend.utility.JWTUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @MockitoBean
    JWTUtility jwtUtility;

    @MockitoBean
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    TaskResponseDTO taskResponseDTO1;
    TaskResponseDTO taskResponseDTO2;




    @BeforeEach
    void setUp() {

        taskResponseDTO1 = new TaskResponseDTO(
                1L,
                "Design Database Schema",
                "Create an efficient database schema for the project management app",
                TaskStatus.IN_PROGRESS,
                null,
                1L
        );

        taskResponseDTO2 = new TaskResponseDTO(
                2L,
                "Implement Authentication",
                "Set up user authentication and authorization using JWT",
                TaskStatus.TODO,
                null,
                1L
        );







    }

    @Nested
    @DisplayName("Get All Tasks")
    class GetAllTasks {


        @Test
        void shouldReturnAllTasksForProject() throws Exception {

            //Arrange
            Long projectId = 1L;
            when(taskService.getAllTasksByProjectId(projectId))
                    .thenReturn(List.of(taskResponseDTO1, taskResponseDTO2));

            //Act
            mockMvc.perform(get("/api/v1/tasks/{projectId}", projectId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].title").value("Design Database Schema"))
                    .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                    .andExpect(jsonPath("$[1].id").value(2L));

        }




    }

    @Nested
    @DisplayName("Get Task By ID")
    class GetTaskById {


        @Test
        void shouldReturnTaskWhenExists() throws Exception {
            //Arrange
            Long taskId = 1L;
            when(taskService.getTaskById(taskId)).thenReturn(taskResponseDTO1);

            //Act & Assert
            mockMvc.perform(get("/api/v1/tasks/task/{taskId}", taskId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Design Database Schema"))
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }


    }

    @Nested
    @DisplayName("Create Task Tests")
    class CreateTask {


        @Test
        void shouldCreateTaskSuccessfully() throws Exception {
            //Arrange
            CreateTaskDTO createTaskDTO = new CreateTaskDTO(
                    "Implement API Endpoints",
                    "Develop RESTful API endpoints for task management",
                    1L
            );
            when(taskService.createTask(createTaskDTO)).thenReturn(taskResponseDTO1);


            //Act
            mockMvc.perform(post("/api/v1/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTaskDTO))
            ).andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Design Database Schema"))
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        }


        @Test
        void shouldReturnBadRequestWhenCreateTaskWithInvalidInput() throws Exception {
            //Arrange
            CreateTaskDTO createTaskDTO = new CreateTaskDTO(
                    "",
                    "Description without title",
                    1L
            );

            //Act & Assert
            mockMvc.perform(post("/api/v1/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTaskDTO))
            ).andExpect(status().isBadRequest());
        }

    }


    @Nested
    @DisplayName("Update Task Tests")
    class UpdateTaskTests {

        @Test
        void shouldUpdateTaskSuccessfully() throws Exception {
            //Arrange
            UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                    "Updated Task Title",
                    "Updated Task Description",
                    TaskStatus.COMPLETED
            );
            Long taskId = 1L;
            TaskResponseDTO updatedTaskResponseDTO = new TaskResponseDTO(
                    1L,
                    "Updated Task Title",
                    "Updated Task Description",
                    TaskStatus.COMPLETED,
                    null,
                    1L
            );
            when(taskService.updateTask(taskId, updateTaskDTO)).thenReturn(updatedTaskResponseDTO);

            //Act
            mockMvc.perform(put("/api/v1/tasks/{taskId}", taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTaskDTO))
            ).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Updated Task Title"))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));

        }

    }


    @Nested
    @DisplayName("Delete Task Tests")
    class DeleteTaskTests {


        @Test
        void shouldDeleteTaskSuccessfully() throws Exception {
           //Arrange
            Long taskId = 1L;


            //Act & Assert
            mockMvc.perform(delete("/api/v1/tasks/{taskId}", taskId))
                    .andExpect(status().isNoContent());

            //Assert
            verify(taskService).deleteTask(taskId);
        }



    }


    @Nested
    @DisplayName("Update Task Status Tests")
    class UpdateTaskStatusTests {

        @Test
        void shouldUpdateTaskStatusSuccessfully() throws Exception {
            //Arrange
            Long taskId = 1L;
            UpdateTaskStatusDTO updateTaskStatusDTO = new UpdateTaskStatusDTO(TaskStatus.COMPLETED);
            TaskResponseDTO updatedTaskResponseDTO = new TaskResponseDTO(
                    1L,
                    "Design Database Schema",
                    "Create an efficient database schema for the project management app",
                    TaskStatus.COMPLETED,
                    null,
                    1L
            );
            when(taskService.updateTaskStatus(taskId, updateTaskStatusDTO)).thenReturn(updatedTaskResponseDTO);


            //Act & Assert
            mockMvc.perform(patch("/api/v1/tasks/{taskId}", taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTaskStatusDTO))
            ).andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Design Database Schema"))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));
        }
    }

}