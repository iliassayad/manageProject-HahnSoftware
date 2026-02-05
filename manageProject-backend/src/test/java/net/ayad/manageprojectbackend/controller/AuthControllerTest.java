package net.ayad.manageprojectbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ayad.manageprojectbackend.dto.AuthResponse;
import net.ayad.manageprojectbackend.dto.LoginRequest;
import net.ayad.manageprojectbackend.service.AuthService;
import net.ayad.manageprojectbackend.service.CustomUserDetailsService;
import net.ayad.manageprojectbackend.service.UserService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JWTUtility jwtUtility;

    @Autowired
    private ObjectMapper objectMapper;

    LoginRequest loginRequest;

    AuthResponse authResponse;

     @BeforeEach
    void setup() {
         loginRequest = new LoginRequest(
                 "john@mail.com",
                 "password123"
         );

         authResponse = new AuthResponse(
                 "fake-jwt-token"
         );
    }

    @Nested
    @DisplayName("Authenticate Tests")
    class AuthenticateTests {
         @Test
         void testAuthenticateSuccess() throws Exception {
             //Arrange
             when(authService.authenticate(loginRequest)).thenReturn(authResponse);

             //Act & Assert
             mockMvc.perform(post("/api/v1/auth/login")
                     .content(objectMapper.writeValueAsString(loginRequest))
                     .contentType(MediaType.APPLICATION_JSON)
             ).andExpect(status().isOk())
             .andExpect(jsonPath("$.token").value("fake-jwt-token"));
         }
    }


}