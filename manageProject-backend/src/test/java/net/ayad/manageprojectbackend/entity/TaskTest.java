package net.ayad.manageprojectbackend.entity;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class TaskTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    void testTaskEntity_whenValidDataProvided_thenCreateTask() {
        //Arrange
        Project project = Project.builder()
                .title("Test Project")
                .description("This is a test project")
                .build();

        Task task = Task.builder()
                .title("Test Task")
                .description("This is a test task")
                .status(TaskStatus.TODO)
                .project(project)
                .build();

        //Act
        testEntityManager.persistAndFlush(project);
        Task savedTask =
            testEntityManager.persistAndFlush(task);


        //Assert
        assertNotNull(savedTask.getId());
        assertEquals(1L, savedTask.getId());


    }


    @Test
    void testTaskEntity_whenProjectIsNull_thenThrowException() {
        //Arrange
        Task task = Task.builder()
                .title("Test Task")
                .description("This is a test task")
                .status(TaskStatus.TODO)
                .build();

        //Act & Assert
        assertThrows(PersistenceException.class, () -> {
            testEntityManager.persistAndFlush(task);
        });
    }

}