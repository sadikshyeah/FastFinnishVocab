package my.project.vocab.seeder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import my.project.vocab.domain.Level;
import my.project.vocab.domain.LevelRepository;
import my.project.vocab.domain.Word;
import my.project.vocab.domain.WordRepository;

@Component
public class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final ObjectMapper objectMapper;

    public DatabaseSeeder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void seed(WordRepository wordRepo, LevelRepository levelRepo) {
        log.info("Seeding database...");

        Level a1 = levelRepo.save(new Level("A1"));
        Level a2 = levelRepo.save(new Level("A2"));
        Level b1 = levelRepo.save(new Level("B1"));
        Level b2 = levelRepo.save(new Level("B2"));
        Level c1 = levelRepo.save(new Level("C1"));
        Level c2 = levelRepo.save(new Level("C2"));

        loadWordsIntoDb(wordRepo, a1, "seeder/seed-words-a1.json");
        loadWordsIntoDb(wordRepo, a2, "seeder/seed-words-a2.json");
        loadWordsIntoDb(wordRepo, b1, "seeder/seed-words-b1.json");
        loadWordsIntoDb(wordRepo, b2, "seeder/seed-words-b2.json");
        loadWordsIntoDb(wordRepo, c1, "seeder/seed-words-c1.json");
        loadWordsIntoDb(wordRepo, c2, "seeder/seed-words-c2.json");

        log.info("Seeding complete!");
    }

    private void loadWordsIntoDb(WordRepository wordRepo, Level level, String classpathResource) {
    
        ClassPathResource resource = new ClassPathResource(classpathResource);

        try (InputStream in = resource.getInputStream()) {
            JsonNode root = objectMapper.readTree(in);
            if (!root.isArray()) {
                throw new IllegalStateException("Expected JSON array in " + classpathResource);
            }
            for (JsonNode node : root) {
                wordRepo.save(new Word(
                        node.path("wordFi").asText(),
                        node.path("definitionEn").asText(),
                        node.path("example").asText(),
                        level));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + classpathResource, e);
        }
    }
}
