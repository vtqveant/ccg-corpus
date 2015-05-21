package ru.eventflow.ccg.data.dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * This class correspond to <b>lemma</b> in OpenCorpora dump!
 */
public class Lexeme {

    protected Form lemma;
    protected List<Form> forms = new ArrayList<Form>();
    protected int id;
    protected int rev;

    public Form getLemma() {
        return lemma;
    }

    public void setLemma(Form lemma) {
        this.lemma = lemma;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void addForm(Form form) {
        forms.add(form);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRev() {
        return rev;
    }

    public void setRev(int rev) {
        this.rev = rev;
    }
}
