package ru.eventflow.ccg.datasource;

import ru.eventflow.ccg.datasource.model.corpus.Sentence;
import ru.eventflow.ccg.datasource.model.corpus.Text;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
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

    public Form getFormById(int id) {
        TypedQuery<Form> query = entityManager.createQuery("SELECT x FROM Form x WHERE x.id = :id", Form.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<String> getOrthographies(String prefix) {
        String q = "SELECT DISTINCT x.orthography FROM Form x WHERE x.lemma = FALSE AND x.orthography LIKE :pattern ORDER BY x.orthography ASC";
        TypedQuery<String> query = entityManager.createQuery(q, String.class);
        query.setMaxResults(50);
        query.setParameter("pattern", prefix + '%');
        return query.getResultList();
    }

    public Sentence getSentenceById(int id) {
        TypedQuery<Sentence> query = entityManager.createQuery("SELECT x FROM Sentence x WHERE x.id = :id", Sentence.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<Sentence> getSentencesByFormId(int formId) {
        String q = "SELECT s.* FROM corpus.token t, corpus.variant v, corpus.sentence s " +
                "WHERE v.form_id = " + formId + " and t.id = v.token_id and t.sentence_id = s.id ORDER BY s.id ASC";
        Query query = entityManager.createNativeQuery(q, Sentence.class);
        return (List<Sentence>) query.getResultList();
    }

    public List<Sentence> getSentencesByFormAndCategory(int formId, String category) {
        String q = "SELECT s.* " +
                "FROM corpus.token AS t, corpus.variant AS v, corpus.sentence AS s, " +
                "     syntax.category_to_form AS cf, syntax.category AS c " +
                "WHERE v.form_id = " + formId +
                "  AND t.id = v.token_id " +
                "  AND t.sentence_id = s.id " +
                "  AND cf.form_id = v.form_id" +
                "  AND cf.category_id = c.id" +
                "  AND c.name = '" + category + "' ORDER BY s.id ASC";
        Query query = entityManager.createNativeQuery(q, Sentence.class);
        return (List<Sentence>) query.getResultList();
    }

    @Deprecated
    public List<Sentence> getSentencesByFormOccurence(Form form) {
        return getSentencesByFormId(form.getId());
    }

    public Map<Form, List<String>> getGrammemes(String form) {
        TypedQuery<Form> query = entityManager.createQuery("SELECT x FROM Form x WHERE x.lemma = FALSE AND x.orthography = :orthography", Form.class);
        query.setParameter("orthography", form);
        List<Form> forms = query.getResultList();
        Map<Form, List<String>> result = new HashMap<>();
        for (Form f : forms) {
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
