package net.ayad.manageprojectbackend.service;

import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                1L,
                "exemple@gmail.com",
                "password123",
                "John",
                "Doe",
                null
        );
    }


    @Nested
    @DisplayName("getCurrentUserId Tests")
    class GetCurrentUserId {

        @Test
        void whenUserIsAuthenticated_thenReturnUserId() {
            //Arrange
            String email = "exemple@gmail.com";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(email);

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

            Mockito.when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(user));

            //Act
            try (MockedStatic<SecurityContextHolder> mockedStatic =
                         Mockito.mockStatic(SecurityContextHolder.class)) {

                mockedStatic.when(SecurityContextHolder::getContext)
                        .thenReturn(securityContext);

                // WHEN
                Long result = authService.getCurrentUserId();

                // THEN
                assertEquals(1L, result);
            }
        }


    }


    @Nested
    @DisplayName("getCurrentUser Tests")
    class GetCurrentUser {

        @Test
        void whenUserExists_thenReturnUser() {
            //Arrange
            String email = "exemple@gmail.com";
            UserDetails userDetails = mock(UserDetails.class);
            when(userDetails.getUsername()).thenReturn(email);

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);

            SecurityContext securityContext = Mockito.mock(SecurityContext.class);
            Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

            Mockito.when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(user));
            Mockito.when(userRepository.findById(1L))
                    .thenReturn(Optional.of(user));
            //Act
            try (MockedStatic<SecurityContextHolder> mockedStatic =
                         Mockito.mockStatic(SecurityContextHolder.class)) {
                mockedStatic.when(SecurityContextHolder::getContext)
                        .thenReturn(securityContext);
                // WHEN
                User result = authService.getCurrentUser();
                // THEN
                assertEquals(user, result);
            }

        }

    }




}