package ru.eventflow.ccg.datasource.model.disambig;

import java.util.ArrayList;
import java.util.List;

public class Sentence {

    private String source;

    private List<Token> tokens = new ArrayList<>();

    private int id;

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
}
