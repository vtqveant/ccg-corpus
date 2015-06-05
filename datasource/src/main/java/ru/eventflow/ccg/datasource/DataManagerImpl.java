package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
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

    public Map<Form, List<Grammeme>> getGrammemes(String form) {
        TypedQuery<Form> query = entityManager.createQuery("SELECT x FROM Form x WHERE x.orthography = :orthography", Form.class);
        query.setParameter("orthography", form);
        List<Form> forms = query.getResultList();
        Map<Form, List<Grammeme>> result = new HashMap<>();
        for (Form f : forms) {
            if (f.isLemma()) continue;
            List<Grammeme> grammemes = f.getGrammemes();
            grammemes.addAll(f.getLexeme().getLemma().getGrammemes());  // add common grammemes
            result.put(f, grammemes);
        }
        return result;
    }

}
