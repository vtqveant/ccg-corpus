package ru.eventflow.annotation.data;

import ru.eventflow.annotation.model.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class DataManager {

    private static final EntityManager enitityManager = Persistence.createEntityManagerFactory("h2-openjpa").createEntityManager();

    public List<Document> getAllDocuments() {
        return enitityManager.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();
    }

    public void setRelevant(Document document) {
        // TODO
        System.out.println(document.getId() + " set relevant");
    }

    public static EntityManager getEnitityManager() {
        return enitityManager;
    }
}
