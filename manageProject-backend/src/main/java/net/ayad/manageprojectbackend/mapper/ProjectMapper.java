package net.ayad.manageprojectbackend.mapper;

import net.ayad.manageprojectbackend.dto.CreateProjectDTO;
import net.ayad.manageprojectbackend.dto.ProjectResponseDTO;
import net.ayad.manageprojectbackend.dto.UpdateProjectDTO;
import net.ayad.manageprojectbackend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toProject(CreateProjectDTO createProjectDTO);

    ProjectResponseDTO toProjectResponseDTO(Project project);

    Project updateProjectFromDTO(UpdateProjectDTO updateProjectDTO,@MappingTarget Project existingProject);
}
