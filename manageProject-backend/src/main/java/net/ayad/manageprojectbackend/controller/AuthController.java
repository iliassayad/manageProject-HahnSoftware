package net.ayad.manageprojectbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.AuthResponse;
import net.ayad.manageprojectbackend.dto.LoginRequest;
import net.ayad.manageprojectbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.authenticate(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
}
