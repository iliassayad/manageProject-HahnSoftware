package net.ayad.manageprojectbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;


    public int getTotalTasks() {
        return tasks != null ? tasks.size() : 0;
    }

    public int getCompletedTasks() {
        if (tasks == null) {
            return 0;
        }
        return (int) tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();
    }

    public double getCompletionPercentage() {
        int totalTasks = getTotalTasks();
        if (totalTasks == 0) {
            return 0.0;
        }
        return (getCompletedTasks() / (double) totalTasks) * 100;
    }
}
