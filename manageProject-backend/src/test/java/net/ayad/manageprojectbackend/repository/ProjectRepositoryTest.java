package net.ayad.manageprojectbackend.repository;

import net.ayad.manageprojectbackend.entity.Project;
import net.ayad.manageprojectbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ProjectRepository projectRepository;

    @Test
    void testFindByIdAndOwnerId_whenProjectExists_thenReturnProject() {
        //Arrange
        User user = User.builder()
                .email("exemple@email.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        Project project = Project.builder()
                .title("Test Project")
                .description("This is a test project")
                .owner(user)
                .build();

        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(project);

        //Act
        Project foundProject = projectRepository.findByIdAndOwnerId(project.getId(), user.getId()).orElse(null);

        //Assert
        assertNotNull(foundProject);
        assertEquals(project.getTitle(), foundProject.getTitle());
        assertEquals(project.getDescription(), foundProject.getDescription());
        assertEquals(project.getOwner().getId(), foundProject.getOwner().getId());
        assertEquals(project.getId(), foundProject.getId());
        assertEquals(user.getId(), foundProject.getOwner().getId());
    }

}