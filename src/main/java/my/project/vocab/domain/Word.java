package my.project.vocab.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "words")
public class Word {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String wordFi;
    
    private String definitionEn;
    private String example;

    public Word() {}

    public Word(String wordFi, String definitionEn, String example) {
        this.wordFi = wordFi;
        this.definitionEn = definitionEn;
        this.example = example;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getWordFi() {
        return wordFi;
    }

    public void setWordFi(String wordFi) {
        this.wordFi = wordFi;
    }

    public String getDefinitionFn() {
        return definitionEn;
    }

    public void setDefinitionFn(String definitionFn) {
        this.definitionEn = definitionFn;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    
}
