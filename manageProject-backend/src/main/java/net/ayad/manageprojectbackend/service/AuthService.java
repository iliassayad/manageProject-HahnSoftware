package net.ayad.manageprojectbackend.service;

import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.AuthResponse;
import net.ayad.manageprojectbackend.dto.LoginRequest;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.UserNotFoundException;
import net.ayad.manageprojectbackend.repository.UserRepository;
import net.ayad.manageprojectbackend.utility.JWTUtility;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtility jwtUtility;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )

        );
        return new AuthResponse(jwtUtility.generateToken(loginRequest.email()));
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userDetails.getUsername()));
        return user.getId();
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
