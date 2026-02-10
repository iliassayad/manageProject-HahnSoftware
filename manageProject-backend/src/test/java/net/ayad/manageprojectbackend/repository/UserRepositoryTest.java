package net.ayad.manageprojectbackend.repository;

import net.ayad.manageprojectbackend.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;


    @Test
    void testFindByEmail_whenEmailExists_thenReturnUser() {
        //Arrange
        User user = User.builder()
                .email("user@mail.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        testEntityManager.persistAndFlush(user);

        //Act
        User foundUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        //Assert
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

}