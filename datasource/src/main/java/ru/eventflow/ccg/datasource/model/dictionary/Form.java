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

    @ManyToMany(cascade=CascadeType.PERSIST)
    @JoinTable(name="form_to_grammeme", joinColumns=@JoinColumn(name="form_id"), inverseJoinColumns=@JoinColumn(name="grammeme_id"))
    private List<Grammeme> grammemes = new ArrayList<Grammeme>();

    @Column(name = "orthography")
    private String orthography;

    @Column(name = "lemma", nullable = false)
    private boolean lemma;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Grammeme> getGrammemes() {
        return grammemes;
    }

    public void addGrammeme(Grammeme grammeme) {
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

    public boolean isLemma() {
        return lemma;
    }

    public void setLemma(boolean lemma) {
        this.lemma = lemma;
    }
}