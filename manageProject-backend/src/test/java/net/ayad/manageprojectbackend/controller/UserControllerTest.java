package net.ayad.manageprojectbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import net.ayad.manageprojectbackend.service.AuthService;
import net.ayad.manageprojectbackend.service.CustomUserDetailsService;
import net.ayad.manageprojectbackend.service.ProjectService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JWTUtility jwtUtility;

    @Autowired
    private ObjectMapper objectMapper;


    CreateUserDTO createUserDTO;

    UserResponseDTO userResponseDTO;

    @BeforeEach
    void setup() {
        createUserDTO = new CreateUserDTO(
                "john@mail.com",
                "password123",
                "John",
                "Doe"
        );

        userResponseDTO = new UserResponseDTO(
                1L,
                "john@mail.com",
                "john",
                "Doe"
        );
    }


    @Nested
    @DisplayName("Create User Tests")
    class CreateUser {


        @Test
        void testCreateUserSuccess() throws Exception {

            //Arrange
            when(userService.createUser(createUserDTO)).thenReturn(userResponseDTO);


            //Act
            mockMvc.perform(post("/api/v1/users/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserDTO))
            ).andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(userResponseDTO.id()))
                    .andExpect(jsonPath("$.email").value(userResponseDTO.email()))
                    .andExpect(jsonPath("$.firstName").value(userResponseDTO.firstName()))
                    .andExpect(jsonPath("$.lastName").value(userResponseDTO.lastName()));
        }
    }




}