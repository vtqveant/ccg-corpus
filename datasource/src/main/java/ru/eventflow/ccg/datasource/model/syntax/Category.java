package ru.eventflow.ccg.datasource.model.syntax;

import ru.eventflow.ccg.datasource.model.corpus.Token;
import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "syntax", name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "category_to_token", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "token_id"))
    private List<Token> tokens = new ArrayList<Token>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "category_to_form", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "form_id"))
    private List<Form> forms = new ArrayList<Form>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }
}
