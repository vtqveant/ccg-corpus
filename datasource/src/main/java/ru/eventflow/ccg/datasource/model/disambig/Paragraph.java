package ru.eventflow.ccg.datasource.model.disambig;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {

    private List<Sentence> sentences = new ArrayList<>();

    private int id;

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
}
