package my.project.vocab.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "levels")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name; 

    @OneToMany(mappedBy = "level")
    private List<Word> words;

    @OneToMany(mappedBy = "level")
    private List<User> users;

    public Level() {}

    public Level(String name) {
        this.name = name;
    }

    // getters & setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Word> getWords() { return words; }

    public void setWords(List<Word> words) { this.words = words; }

    public List<User> getUsers() { return users; }

    public void setUsers(List<User> users) { this.users = users; }
}