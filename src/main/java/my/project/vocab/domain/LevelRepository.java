package my.project.vocab.domain;

import org.springframework.data.repository.CrudRepository;

public interface LevelRepository extends CrudRepository<Level, Long> {
    Level findByName(String name);
    
}