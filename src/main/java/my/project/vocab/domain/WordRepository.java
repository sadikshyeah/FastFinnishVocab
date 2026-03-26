package my.project.vocab.domain;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface WordRepository extends CrudRepository<Word, Long> {
    List<Word> findByLevelName(String name);

    List<Word> findByWordFi(String wordFi);

}
