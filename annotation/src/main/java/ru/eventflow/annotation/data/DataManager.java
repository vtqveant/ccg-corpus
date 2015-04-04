package ru.eventflow.annotation.data;

import ru.eventflow.annotation.model.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Ходим в БД
 */
public class DataManager {

    static final EntityManager enitityManager = Persistence.createEntityManagerFactory("h2-openjpa").createEntityManager();

    public static List<Document> getAllDocuments() {
        List<Document> documents = enitityManager.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();
        return documents;
    }

    public static void setRelevant(int documentId) {
        // TODO
        // enitityManager.createQuery("UPDATE ");
    }

    public static EntityManager getEnitityManager() {
        return enitityManager;
    }
}
