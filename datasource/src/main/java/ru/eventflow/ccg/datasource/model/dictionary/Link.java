package ru.eventflow.ccg.datasource.model.dictionary;

import javax.persistence.*;

@Entity
@Table(schema = "dictionary", name = "link")
public class Link {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @ManyToOne(targetEntity = Lexeme.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "from_id", nullable = false)
    private Lexeme from;

    @ManyToOne(targetEntity = Lexeme.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "to_id", nullable = false)
    private Lexeme to;

    @ManyToOne(targetEntity = LinkType.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = false)
    private LinkType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lexeme getFrom() {
        return from;
    }

    public void setFrom(Lexeme from) {
        this.from = from;
    }

    public Lexeme getTo() {
        return to;
    }

    public void setTo(Lexeme to) {
        this.to = to;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }
}
