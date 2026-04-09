package my.project.vocab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import my.project.vocab.domain.User;
import my.project.vocab.domain.UserRepository;
import my.project.vocab.domain.WordRepository;
import my.project.vocab.domain.LevelRepository;
import my.project.vocab.seeder.DatabaseSeeder;

@SpringBootApplication
public class VocabApplication {

    private static final Logger log = LoggerFactory.getLogger(VocabApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(VocabApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(DatabaseSeeder seeder, WordRepository wordRepo, LevelRepository levelRepo) {
        return (args) -> {
            if (args.length > 0 && args[0].equals("seed")) {
                log.info("Seeding database with initial data...");
                seeder.seed(wordRepo, levelRepo);
            }
        };
    }

    @Bean
    public CommandLineRunner userDemo(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            log.info("Creating users");

            if (userRepository.findByUsername("admin") == null) {
                userRepository.save(new User(
                        "admin",
                        passwordEncoder.encode("admin"),
                        "admin@VocabApplication.com",
                        "ROLE_ADMIN", null, true));
            }

            if (userRepository.findByUsername("user") == null) {
                userRepository.save(new User(
                        "user",
                        passwordEncoder.encode("user"),
                        "user@VocabApplication.com",
                        "ROLE_USER", null, true));
            }

            log.info("Users created successfully");
        };
    }
}