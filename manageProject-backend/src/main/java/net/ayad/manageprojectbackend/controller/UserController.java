package net.ayad.manageprojectbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import net.ayad.manageprojectbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(createUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }
}
