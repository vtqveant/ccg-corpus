package ru.eventflow.ccg.datasource.model.disambig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "sentence")
public class Sentence {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @Column(name = "source", columnDefinition = "TEXT")
    private String source;

    @OneToMany(targetEntity = Token.class, mappedBy = "sentence", cascade = CascadeType.ALL)
    private List<Token> tokens = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "paragraph_id", nullable = false)
    private Paragraph paragraph;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Paragraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }
}
