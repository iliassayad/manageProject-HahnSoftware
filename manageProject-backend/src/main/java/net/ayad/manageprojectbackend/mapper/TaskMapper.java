package net.ayad.manageprojectbackend.mapper;

import net.ayad.manageprojectbackend.dto.CreateTaskDTO;
import net.ayad.manageprojectbackend.dto.TaskResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateTaskDTO;
import net.ayad.manageprojectbackend.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ProjectMapper.class)
public interface TaskMapper {
    Task toTask(CreateTaskDTO createTaskDTO);

    @Mapping(source = "project.id", target = "projectId")
    TaskResponseDTO toTaskResponseDTO(Task task);

    Task updateTaskFromDTO(UpdateTaskDTO updateTaskDTO, @MappingTarget Task existingTask);


}
