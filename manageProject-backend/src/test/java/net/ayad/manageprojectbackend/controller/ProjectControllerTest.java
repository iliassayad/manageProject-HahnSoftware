package net.ayad.manageprojectbackend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.service.CustomUserDetailsService;
import net.ayad.manageprojectbackend.service.ProjectService;
import net.ayad.manageprojectbackend.utility.JWTUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@WebMvcTest(controllers = ProjectController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JWTUtility jwtUtility;

    @Autowired
    private ObjectMapper objectMapper;

    ProjectResponseDTO project1;
    ProjectResponseDTO project2;

    CreateProjectDTO createProjectDTO;

    UpdateProjectDTO updateProjectDTO;


    @BeforeEach
    void setup() {
        project1 = new ProjectResponseDTO(
                1L,
                "Project Title",
                "Project Description",
                null,
                10,
                5,
                50.0
        );
        project2 = new ProjectResponseDTO(
                2L,
                "Another Project",
                "Another Description",
                null,
                8,
                8,
                100.0
        );

        createProjectDTO = new CreateProjectDTO(
                "New Project",
                "New Project Description"
        );

        updateProjectDTO = new UpdateProjectDTO(
                "Updated Project Title",
                "Updated Project Description"
        );
    }

    @Nested
    @DisplayName("Get All Projects Tests")
    class GetAllProjects {


        @Test
        void testGetAllProjects_whenProjectsExist_thenReturnListOfProjects() throws Exception {
            //Arrange
            when(projectService.getAllProjects()).thenReturn(List.of(project1, project2));

            //Act & Assert
            mockMvc.perform(get("/api/v1/projects"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].title").value("Project Title"))
                    .andExpect(jsonPath("$[1].id").value(2L))
                    .andExpect(jsonPath("$[1].title").value("Another Project"));
        }

    }

    @Nested
    @DisplayName("Get Project By Id Tests")
    class GetProjectById {

        @Test
        void testGetProjectById_whenProjectExists_thenReturnProject() throws Exception {
            //Arrange
            when(projectService.getProjectById(1L)).thenReturn(project1);

            //Act & Assert
            mockMvc.perform(get("/api/v1/projects/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Project Title"));
        }


    }

    @Nested
    @DisplayName("Create Project Tests")
    class CreateProjectTests {


        @Test
        void testCreateProject_whenValidInput_thenReturnCreatedProject() throws Exception {
            //Arrange
            when(projectService.createProject(createProjectDTO)).thenReturn(project1);

            //Act
             MvcResult result = mockMvc.perform(post("/api/v1/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createProjectDTO)))
                    .andReturn();

            //Assert

            assertEquals(201, result.getResponse().getStatus());
            assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
            String responseBody = result.getResponse().getContentAsString();
            ProjectResponseDTO returnedProject = objectMapper.readValue(responseBody, ProjectResponseDTO.class);
            assertEquals(1L, returnedProject.id());
            assertEquals("Project Title", returnedProject.title());





        }
    }


    @Nested
    @DisplayName("Delete Project Tests")
    class DeleteProject {

        @Test
        void testDeleteProject_whenProjectExists_thenReturnNoContent() throws Exception {
            //Arrange
            Long projectId = 1L;

            //Act & Assert
            mockMvc.perform(delete("/api/v1/projects/{id}", projectId))
                    .andExpect(status().isNoContent());

            verify(projectService).deleteProject(projectId);
        }

    }

    @Nested
    @DisplayName("Update Project Tests")
    class UpdateProjectTests {
        @Test
        void testUpdateProject_whenValidInput_thenReturnUpdatedProject() throws Exception {
            //Arrange
            Long projectId = 1L;
            when(projectService.updateProject(projectId, updateProjectDTO)).thenReturn(project1);


            //Act & Assert
            mockMvc.perform(put("/api/v1/projects/{id}", projectId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateProjectDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.title").value("Project Title"));
        }
    }







}