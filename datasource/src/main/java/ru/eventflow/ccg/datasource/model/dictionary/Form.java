package ru.eventflow.ccg.datasource.model.dictionary;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lemma is a special kind of form, it contains a normal form orthography
 * and a set of grammemes common to all other forms.
 */
@Entity
@Table(schema = "dictionary", name = "form")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lexeme_id", nullable = false)
    private Lexeme lexeme;

    private List<String> grammemes = new ArrayList<String>();

    @Column(name = "orthography")
    private String orthography;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getGrammemes() {
        return grammemes;
    }

    public void addGrammeme(String grammeme) {
        grammemes.add(grammeme);
    }

    public String getOrthography() {
        return orthography;
    }

    public void setOrthography(String orthography) {
        this.orthography = orthography;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public void setLexeme(Lexeme lexeme) {
        this.lexeme = lexeme;
    }
}
