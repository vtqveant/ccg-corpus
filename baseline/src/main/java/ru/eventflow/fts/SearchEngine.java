package ru.eventflow.fts;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {

    private EntityManager em;

    public SearchEngine(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public Result executeQuery(String query) {
        Query q;
        if (query.startsWith("\"") && query.endsWith("\"")) { // if this is an exact match query
            query = query.substring(1, query.length() - 1);
            q = em.createNativeQuery("SELECT id FROM document WHERE text LIKE '%" + query + "%' ORDER BY id ASC;", Integer.class);
        } else {
            q = em.createNativeQuery("SELECT id FROM document WHERE text @@ plainto_tsquery('" + query + "') ORDER BY id ASC;", Integer.class);
        }
        return new Result(query, (List<Integer>) q.getResultList());
    }



}
