package ru.eventflow.fts;

import ru.eventflow.fts.datasource.Query;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Map;

public class DBInitializer {

    public static final String DUMP_LOCATION = "/home/transcend/code/NLU-RG/tracks/track1/baseline/baseline/src/main/resources/annot.opcorpora.xml.byfile";
    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) throws IOException {
        // 1. process the OpenCorpora dump
        SourceProcessor sourceProcessor = new SourceProcessor(em, DUMP_LOCATION, false);
        sourceProcessor.init();

        // 3. process queries
        RequestsCSVLoader requestsLoader = new RequestsCSVLoader();
        em.getTransaction().begin();
        for (Map.Entry<String, String> entry : requestsLoader.getFullRequests().entrySet()) {
            Query query = new Query(entry.getKey(), entry.getValue());
            em.persist(query);
        }
        em.getTransaction().commit();

        // 2. process assessments
        // TODO
        // RelevanceLoader relevanceLoader = new RelevanceLoader();
    }


}
