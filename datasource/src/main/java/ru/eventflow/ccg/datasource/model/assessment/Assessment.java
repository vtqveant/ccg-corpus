package ru.eventflow.ccg.datasource.model.assessment;

import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.persistence.*;

@Entity
@Table(schema = "assessment", name = "assessment", uniqueConstraints = {@UniqueConstraint(columnNames = {"query_id", "document_id"})})
public class Assessment {

    @Basic
    private boolean relevant;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "query_id", columnDefinition = "int4", nullable = false)
    private Query query;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "document_id", columnDefinition = "int4", nullable = false)
    private Document document;

    public Assessment() {
    }

    public boolean isRelevant() {
        return relevant;
    }

    public void setRelevant(boolean relevant) {
        this.relevant = relevant;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
