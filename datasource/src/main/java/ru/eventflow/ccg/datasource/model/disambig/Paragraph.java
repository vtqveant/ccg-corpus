package ru.eventflow.ccg.datasource.model.disambig;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "paragraph")
public class Paragraph {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @OneToMany(targetEntity = Sentence.class, mappedBy = "paragraph", cascade = CascadeType.ALL)
    private List<Sentence> sentences = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "text_id", nullable = false)
    private Text text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
