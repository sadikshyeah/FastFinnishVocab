package my.project.vocab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}