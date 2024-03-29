package ru.eventflow.ccg.datasource.model.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "dictionary", name = "link_type")
public class LinkType {

    @Id
    @Column(columnDefinition = "serial")
    private int id;

    @Column(name = "type")
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
