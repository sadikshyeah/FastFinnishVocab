package my.project.vocab.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import my.project.vocab.domain.Word;
import my.project.vocab.domain.WordRepository;
import my.project.vocab.domain.Level;
import my.project.vocab.domain.LevelRepository;

@Component
public class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    public void seed(WordRepository wordRepo, LevelRepository levelRepo) {
        log.info("Seeding database...");

        // Create Levels
        Level a1 = levelRepo.save(new Level("A1"));
        Level a2 = levelRepo.save(new Level("A2"));
        
        levelRepo.save(new Level("B1"));
        levelRepo.save(new Level("B2"));
        levelRepo.save(new Level("C1"));
        levelRepo.save(new Level("C2"));

        // Create Words
        wordRepo.save(new Word("kissa", "cat", "Kissa on söpö", a1));
        wordRepo.save(new Word("koira", "dog", "Koira haukkuu", a1));
        wordRepo.save(new Word("rakastaa", "to love", "Minä rakastan sinua", a2));

        log.info("Seeding complete!");
    }
}