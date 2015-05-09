package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.Document;

import java.util.List;

public interface DataManager {
    List<Document> getAllDocuments();

    void setRelevant(Document document);

    void setNonrelevant(Document document);
}
