package net.ayad.manageprojectbackend.service;

import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.EmailTakenException;
import net.ayad.manageprojectbackend.mapper.UserMapper;
import net.ayad.manageprojectbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserMapper userMapper;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    CreateUserDTO createUserDTO;
    User user;
    UserResponseDTO userResponseDTO;

    @BeforeEach
    void setup() {
        createUserDTO = new CreateUserDTO(
                "user@mail.com",
                "password123",
                "John",
                "Doe"
        );

        user = User.builder()
                .id(1L)
                .email("user@mail.com")
                .password("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .build();

        userResponseDTO = new UserResponseDTO(
                1L,
                "user@mail.com",
                "John",
                "Doe"
        );
    }

    @DisplayName("create user tests")
    @Nested
    class CreateUserTests {

        @Test
        void whenInputIsValid_thenCreateUser() {
           //Arrange
            when(userMapper.toUser(createUserDTO)).thenReturn(user);
            when(passwordEncoder.encode(createUserDTO.password())).thenReturn("encodedPassword");
            when(userRepository.findByEmail(createUserDTO.email())).thenReturn(Optional.empty());
            when(userRepository.save(user)).thenReturn(user);
            when(userMapper.toUserResponseDTO(user)).thenReturn(userResponseDTO);

            //Act
            UserResponseDTO result = userService.createUser(createUserDTO);

            //Assert
            verify(userRepository).save(user);
        }

        @Test
        void whenEmailIsTaken_thenThrowException() {
            //Arrange
            when(userRepository.findByEmail(createUserDTO.email())).thenReturn(Optional.of(user));

            //Act & Assert
            EmailTakenException exception = assertThrows(EmailTakenException.class, () ->
                    userService.createUser(createUserDTO)
                    );

            //Assert
            assertEquals("Email already taken: " + createUserDTO.email(), exception.getMessage());
            verifyNoInteractions(userMapper);
            verifyNoInteractions(passwordEncoder);
            verify(userRepository, never()).save(user);
        }
    }

  
}