package my.project.vocab.domain;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByResetToken(String token);

    User findByVerificationToken(String token);
}
