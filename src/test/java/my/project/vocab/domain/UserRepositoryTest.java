package my.project.vocab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void userCreate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername("testuser_" + suffix);
        user.setEmail("test_" + suffix + "@example.com");
        user.setPassword("testPassword");
        user.setRole("ROLE_USER");
        user.setEnabled(false);

        userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(userRepository.findByEmail(user.getEmail())).isNotNull();
    }

    @Test
    @Transactional
    void userUpdate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername("before_" + suffix);
        user.setEmail("update_" + suffix + "@example.com");
        user.setPassword("testPassword");
        user.setRole("ROLE_USER");
        user.setEnabled(false);
        userRepository.save(user);

        User found = userRepository.findByEmail(user.getEmail());
        found.setUsername("after_" + suffix);
        userRepository.save(found);

        User updated = userRepository.findByEmail(user.getEmail());
        assertThat(updated).isNotNull();
        assertThat(updated.getUsername()).isEqualTo("after_" + suffix);
    }

    @Test
    @Transactional
    void userDelete() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        User user = new User();
        user.setUsername("delete_" + suffix);
        user.setEmail("delete_" + suffix + "@example.com");
        user.setPassword("testPassword");
        user.setRole("ROLE_USER");
        user.setEnabled(false);
        userRepository.save(user);

        User found = userRepository.findByEmail(user.getEmail());
        userRepository.deleteById(found.getId());

        assertThat(userRepository.findByEmail(user.getEmail())).isNull();
    }
}
