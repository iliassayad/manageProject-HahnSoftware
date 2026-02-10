package net.ayad.manageprojectbackend.integrationTests;

import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testUserRegistration_whenValidDetailsProvided_thenUserRegistered() {
        //Arrange
        CreateUserDTO createUserDTO = new CreateUserDTO(
                "testuser@example.com",
                "password123",
                "Test",
                "User"
        );
//        JSONObject requestBody = new JSONObject();
//        requestBody.put("email", createUserDTO.email());
//        requestBody.put("password", createUserDTO.password());
//        requestBody.put("firstName", createUserDTO.firstName());
//        requestBody.put("lastName", createUserDTO.lastName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<CreateUserDTO> requestEntity = new HttpEntity<>(createUserDTO, headers);




        //Act

        ResponseEntity<UserResponseDTO> createdUser = testRestTemplate.postForEntity("/api/v1/users/create", requestEntity, UserResponseDTO.class);

        UserResponseDTO responseBody = createdUser.getBody();



        //Assert
        assertEquals(HttpStatus.CREATED, createdUser.getStatusCode());
        assertEquals(1L, responseBody.id());
        assertEquals(createUserDTO.email(), responseBody.email());
        assertEquals(createUserDTO.firstName(), responseBody.firstName());
        assertEquals(createUserDTO.lastName(), responseBody.lastName());
    }
}
