package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManagerImpl implements DataManager {

    private final EntityManager entityManager;

    public DataManagerImpl(String persistenceUnitName) {
        entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
    }

    public List<Text> getAllTexts() {
        return entityManager.createQuery("SELECT x FROM Text x ORDER BY x.id ASC", Text.class).getResultList();
    }

    public List<String> getOrthographies(String prefix) {
        String q = "SELECT DISTINCT x.orthography FROM Form x WHERE x.lemma = FALSE AND x.orthography LIKE :pattern ORDER BY x.orthography ASC";
        TypedQuery<String> query = entityManager.createQuery(q, String.class);
        query.setMaxResults(50);
        query.setParameter("pattern", prefix + '%');
        return query.getResultList();
    }

    public Map<Form, List<String>> getGrammemes(String form) {
        TypedQuery<Form> query = entityManager.createQuery("SELECT x FROM Form x WHERE x.orthography = :orthography", Form.class);
        query.setParameter("orthography", form);
        List<Form> forms = query.getResultList();
        Map<Form, List<String>> result = new HashMap<>();
        for (Form f : forms) {
            if (f.isLemma()) {
                continue;
            }
            List<String> grammemes = new ArrayList<>();
            for (Grammeme g : f.getGrammemes()) {
                grammemes.add(g.getName());
            }
            // add common grammemes
            for (Grammeme g : f.getLexeme().getLemma().getGrammemes()) {
                grammemes.add(g.getName());
            }
            result.put(f, grammemes);
        }
        return result;
    }

}
