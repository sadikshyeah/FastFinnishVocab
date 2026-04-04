package my.project.vocab.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface WordRepository extends CrudRepository<Word, Long> {
    List<Word> findByLevelName(String name);

    List<Word> findByWordFi(String wordFi);

    @Query(value = """
            SELECT * FROM words
            WHERE levels_id = :levelId
            ORDER BY RANDOM()
            LIMIT 1
            """, nativeQuery = true)
    Optional<Word> findRandomByLevelId(@Param("levelId") Long levelId);

}
