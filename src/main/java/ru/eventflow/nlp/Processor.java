package ru.eventflow.nlp;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class Processor {

    private EntityManager em;

    public Processor(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List<Integer> executeQuery(String query) {
        Query q;
        if (query.startsWith("\"") && query.endsWith("\"")) { // if this is an exact match query
            query = query.substring(1, query.length() - 1);
            q = em.createNativeQuery("SELECT id FROM document WHERE text LIKE '%" + query + "%';", Integer.class);
        } else {
            q = em.createNativeQuery("SELECT id FROM document WHERE text @@ plainto_tsquery('" + query + "');", Integer.class);
        }
        return (List<Integer>) q.getResultList();
    }

}
