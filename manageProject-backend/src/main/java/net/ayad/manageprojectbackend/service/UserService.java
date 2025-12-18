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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        User user = userMapper.toUser(createUserDTO);
        if (isEmailTaken(user.getEmail())) {
            throw new EmailTakenException(createUserDTO.email());
        }
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(savedUser);
    }


    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
