package net.ayad.manageprojectbackend.service;

import lombok.RequiredArgsConstructor;
import net.ayad.manageprojectbackend.entity.User;
import net.ayad.manageprojectbackend.exception.UserNotFoundException;
import net.ayad.manageprojectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    public Long getCurrentUserId() {
        // For simplicity, returning a fixed user ID.
        return 2L;
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
