package net.ayad.manageprojectbackend.repository;

import net.ayad.manageprojectbackend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository  extends JpaRepository<Task, Long> {
    List<Task> findAllByProjectId(Long id);
}
