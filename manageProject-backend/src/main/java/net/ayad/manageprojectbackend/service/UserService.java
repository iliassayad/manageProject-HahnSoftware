package net.ayad.manageprojectbackend.service;

import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.EmailTakenException;
import net.ayad.manageprojectbackend.mapper.UserMapper;
import net.ayad.manageprojectbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        if (isEmailTaken(createUserDTO.email())) {
            throw new EmailTakenException(createUserDTO.email());
        }
        User user = userMapper.toUser(createUserDTO);
        user.setPassword(passwordEncoder.encode(createUserDTO.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(savedUser);
    }


    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
