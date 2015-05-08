package ru.eventflow.ccg.data;

import ru.eventflow.ccg.model.Document;

import java.util.List;

public interface DataManager {
    List<Document> getAllDocuments();

    void setRelevant(Document document);

    void setNonrelevant(Document document);
}
