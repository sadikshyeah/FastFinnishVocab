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
import my.project.vocab.domain.Word;
import my.project.vocab.domain.WordRepository;

@SpringBootApplication
public class VocabApplication {

	private static final Logger log = LoggerFactory.getLogger(VocabApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(VocabApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(WordRepository repository) {
		return (args) -> {
			log.info("save a couple of words");

			repository.save(new Word("kissa", "cat", "Kissa on söpö"));
			repository.save(new Word("koira", "dog", "Koira haukkuu"));

			log.info("fetch all words");
			for (Word word : repository.findAll()) {
				log.info(word.toString());
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
                "ROLE_ADMIN"
            ));
        }

        if (userRepository.findByUsername("user") == null) {
            userRepository.save(new User(
                "user",
                passwordEncoder.encode("user"),
                "user@VocabApplication.com",
                "ROLE_USER"
            ));
        }

        log.info("Users created successfully");
    };
}
}