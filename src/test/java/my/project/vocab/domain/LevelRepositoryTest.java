package my.project.vocab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class LevelRepositoryTest {

    @Autowired
    private LevelRepository levelRepository;

    @Test
    @Transactional
    void levelCreate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = new Level("TEST_LEVEL_" + suffix);

        levelRepository.save(level);

        assertThat(level.getId()).isNotNull();
        assertThat(levelRepository.findByName(level.getName())).isNotNull();
    }

    @Test
    @Transactional
    void levelUpdate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = new Level("OLD_LEVEL_" + suffix);
        levelRepository.save(level);

        Level found = levelRepository.findByName("OLD_LEVEL_" + suffix);
        found.setName("NEW_LEVEL_" + suffix);
        levelRepository.save(found);

        assertThat(levelRepository.findByName("OLD_LEVEL_" + suffix)).isNull();
        assertThat(levelRepository.findByName("NEW_LEVEL_" + suffix)).isNotNull();
    }

    @Test
    @Transactional
    void levelDelete() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = new Level("DELETE_LEVEL_" + suffix);
        levelRepository.save(level);

        Level found = levelRepository.findByName("DELETE_LEVEL_" + suffix);
        levelRepository.deleteById(found.getId());

        assertThat(levelRepository.findByName("DELETE_LEVEL_" + suffix)).isNull();
    }
}
