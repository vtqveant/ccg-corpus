package ru.eventflow.ccg.datasource.model.corpus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "corpus", name = "paragraph")
public class Paragraph {

    @Id
    private int id;

    @OneToMany(targetEntity = Sentence.class, mappedBy = "paragraph", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private List<Sentence> sentences = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "text_id", nullable = false)
    private Text text;

    @Column(name = "pos")
    private int position;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
