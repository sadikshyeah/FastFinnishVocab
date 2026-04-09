package my.project.vocab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Test
    @Transactional
    void wordCreate() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = levelRepository.save(new Level("WORD_CREATE_LEVEL_" + suffix));

        Word word = new Word("sana_" + suffix, "word", "example sentence", level);
        wordRepository.save(word);

        assertThat(word.getId()).isNotNull();
        assertThat(wordRepository.findByWordFi("sana_" + suffix)).isNotEmpty();
    }

    @Test
    @Transactional
    void wordDelete() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = levelRepository.save(new Level("WORD_DELETE_LEVEL_" + suffix));
        Word word = wordRepository.save(new Word("poista_" + suffix, "delete", "delete example", level));

        wordRepository.deleteById(word.getId());

        assertThat(wordRepository.findByWordFi("poista_" + suffix)).isEmpty();
    }

    @Test
    @Transactional
    void findRandomByLevelIdReturnsWordForLevel() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Level level = levelRepository.save(new Level("RANDOM_LEVEL_" + suffix));
        wordRepository.save(new Word("random1_" + suffix, "random one", "ex1", level));
        wordRepository.save(new Word("random2_" + suffix, "random two", "ex2", level));

        Optional<Word> randomWord = wordRepository.findRandomByLevelId(level.getId());

        assertThat(randomWord).isPresent();
        assertThat(randomWord.get().getLevel().getId()).isEqualTo(level.getId());
    }
}
