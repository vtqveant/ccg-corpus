package ru.eventflow.fts;

import ru.eventflow.fts.csv.RequestsCSVReader;
import ru.eventflow.fts.datasource.Query;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;

public class DBInitializer {

    public static final String DUMP_LOCATION = "/home/transcend/code/NLU-RG/tracks/track1/data/annot.opcorpora.xml.byfile";
    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) throws IOException {
        // 1. process the OpenCorpora dump
        CorpusDataPreprocessor corpusDataPreprocessor = new CorpusDataPreprocessor(em, DUMP_LOCATION, false);
        corpusDataPreprocessor.init();

        // 3. process queries
        RequestsCSVReader requestsCSVReader = new RequestsCSVReader();
        em.getTransaction().begin();
        for (Request request : requestsCSVReader.getRequests()) {
            Query query = new Query(request.getText(), request.getDescription());
            em.persist(query);
        }
        em.getTransaction().commit();
    }

}
