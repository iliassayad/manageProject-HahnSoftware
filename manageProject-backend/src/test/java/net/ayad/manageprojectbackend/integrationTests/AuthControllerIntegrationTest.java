package net.ayad.manageprojectbackend.integrationTests;


import net.ayad.manageprojectbackend.dto.AuthResponse;
import net.ayad.manageprojectbackend.dto.LoginRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;


    @Order(1)
    @Test
    void testUserLogin_whenValidCredentialsProvided_thenReturnsAuthResponse() {
        //Arrange
        LoginRequest loginRequest = new LoginRequest(
                "testuser@example.com",
                "password123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);


        //Act

        ResponseEntity<AuthResponse> response =
                testRestTemplate.postForEntity("/api/v1/auth/login", requestEntity, AuthResponse.class);

        AuthResponse authResponse = response.getBody();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(authResponse);
        assertNotNull(authResponse.token());
    }

    @Order(2)
    @Test
    void testUserLogin_whenInvalidCredentialsProvided_thenReturnsUnauthorized() {
        //Arrange
        LoginRequest loginRequest = new LoginRequest(
                "testuser@example.com",
                "wrongpassword"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

        //Act
        ResponseEntity<AuthResponse> response =
                testRestTemplate.postForEntity("/api/v1/auth/login", requestEntity, AuthResponse.class);

        //Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
