package net.ayad.manageprojectbackend;

import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.entity.Project;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ManageProjectBackendApplicationTests {

    @LocalServerPort
    private int portNumber;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    void contextLoads() {
        System.out.println("Running tests on port: " + portNumber);
    }

    @Test
    @DisplayName("Create Project - Valid Details")
    void testCreateProject_whenValidDetailsProvided_thenProjectCreated() throws JSONException {
        //Arrange
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", "New Project");
        requestBody.put("description", "Project Description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);


        //Act
        ResponseEntity<ProjectResponseDTO>  createdProject = testRestTemplate.postForEntity("/api/v1/projects", requestEntity, ProjectResponseDTO.class);

        ProjectResponseDTO responseBody = createdProject.getBody();

        //Assert

        assertEquals(HttpStatus.CREATED, createdProject.getStatusCode());
    }

}
