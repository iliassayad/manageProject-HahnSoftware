package net.ayad.manageprojectbackend.repository;

import net.ayad.manageprojectbackend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByIdAndOwnerId(Long id, Long id1);

    List<Project> findAllByOwnerId(Long currentUserId);
}
