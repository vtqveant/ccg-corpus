package ru.eventflow.ccg.parser;

import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.dictionary.Form;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.syntax.Category;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Preliminary mapping of grammemes to syntactic categories
 * Adds synt. categories to the DB as a side-effect
 */
public class Mapper {

    private static EntityManager em = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();

    public static List<SynCat> map(int formId) {

        Form form = em.find(Form.class, formId);
        em.getTransaction().begin();

        List<String> grammemes = new ArrayList<>();
        for (Grammeme grammeme : form.getGrammemes()) {
            grammemes.add(grammeme.getName());
        }
        for (Grammeme grammeme : form.getLexeme().getLemma().getGrammemes()) {
            grammemes.add(grammeme.getName());
        }

        List<String> cats = new ArrayList<>();
        if (grammemes.contains("NOUN")) {
            cats.add("n");         // школа
        }
        if (grammemes.contains("NOUN") && grammemes.contains("gent")) {
            cats.add("n\\n");      // (школа) злословия
        }
        if (grammemes.contains("ADJF")) {
            cats.add("n/n");
        }
        if (grammemes.contains("VERB") && grammemes.contains("tran")) {
            cats.add("(s\\n)/np"); // (мальчик) читает (книгу)
            cats.add("(s\\n)/s");  // учит (прикусить язык)
        }
        if (grammemes.contains("INFN")) {
            cats.add("s/n");
        }
        if (grammemes.contains("PNCT")) {
            cats.add("n/n");       // << (школа)
            cats.add("n\\n");      // (злословия) >>
            cats.add("(n\\n)/n");  // (шпаги) , (деньги)
        }
        if (grammemes.contains("CONJ")) {
            cats.add("(n\\n)/n");  // (шпаги) и (деньги)
        }

        List<SynCat> result = new ArrayList<>();
        for (String cat : cats) {
            SynCat synCat = CategoryBuilder.build(cat);

            TypedQuery<Category> query = em.createQuery("SELECT x FROM Category x WHERE x.name = :name", Category.class);
            query.setParameter("name", synCat.getName());
            List<Category> rs = query.getResultList();
            Category category;
            if (!rs.isEmpty()) {
                category = rs.get(0);
            } else {
                category = new Category();
                category.setName(synCat.getName());
            }
            category.getForms().add(form);
            em.persist(category);

            result.add(synCat);
        }
        em.getTransaction().commit();
        return result;
    }
}
