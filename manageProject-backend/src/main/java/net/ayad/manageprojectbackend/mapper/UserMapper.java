package net.ayad.manageprojectbackend.mapper;

import net.ayad.manageprojectbackend.dto.CreateUserDTO;
import net.ayad.manageprojectbackend.dto.UserResponseDTO;
import net.ayad.manageprojectbackend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserDTO createUserDTO);

    UserResponseDTO toUserResponseDTO(User savedUser);
}
