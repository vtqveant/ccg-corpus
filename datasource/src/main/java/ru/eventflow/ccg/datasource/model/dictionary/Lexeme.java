package ru.eventflow.ccg.datasource.model.dictionary;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class correspond to <b>lemma</b> in OpenCorpora dump!
 */
@Entity
@Table(schema = "dictionary", name = "lexeme")
public class Lexeme {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @OneToOne(targetEntity = Form.class, cascade = CascadeType.PERSIST)
    private Form lemma;

    @OneToMany(targetEntity = Form.class, mappedBy = "lexeme", cascade = CascadeType.PERSIST)
    private Set<Form> forms = new HashSet<Form>();

    @Column(name = "rev")
    private int rev;

    public Form getLemma() {
        return lemma;
    }

    public void setLemma(Form lemma) {
        this.lemma = lemma;
    }

    public Set<Form> getForms() {
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
