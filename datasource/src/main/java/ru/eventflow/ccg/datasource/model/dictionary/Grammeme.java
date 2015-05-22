package ru.eventflow.ccg.datasource.model.dictionary;

import javax.persistence.*;

@Entity
@Table(schema = "dictionary", name = "grammeme")
public class Grammeme {

    @Id
    @Column(name = "name", length = 4)
    private String name;

    @Column(name = "alias")
    private String alias;

    @Column(name = "description")
    private String description;

    @ManyToOne(targetEntity = Grammeme.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", nullable = true)
    private Grammeme parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Grammeme getParent() {
        return parent;
    }

    public void setParent(Grammeme parent) {
        this.parent = parent;
    }

}
