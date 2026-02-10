package net.ayad.manageprojectbackend.integrationTests;

import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProjectControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;


    @Test
    void testGetAllProjects_whenJwtTokenNotProvided_thenReturnsForbidden()  {
        //Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);


        //Act
        ResponseEntity<ProjectResponseDTO[]> responseEntity =
                    testRestTemplate.exchange("/api/v1/projects", HttpMethod.GET, requestEntity, ProjectResponseDTO[].class);


        //Assert
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }
}
