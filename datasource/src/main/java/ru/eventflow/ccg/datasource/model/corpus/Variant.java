package ru.eventflow.ccg.datasource.model.corpus;

import ru.eventflow.ccg.datasource.model.dictionary.Form;

import javax.persistence.*;

@Entity
@Table(schema = "corpus", name = "variant")
public class Variant {

    @Id
    @Column(columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = Form.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "token_id", nullable = false)
    private Token token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
