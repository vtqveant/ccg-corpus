package ru.eventflow.nlp;

import ru.eventflow.nlp.model.Document;
import ru.eventflow.nlp.xml.Paragraph;
import ru.eventflow.nlp.xml.Sentence;
import ru.eventflow.nlp.xml.Text;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Runner {

    private static Unmarshaller unmarshaller;

    private static final Logger logger = Logger.getLogger("Runner");
    private static EntityManager em;

    static {
        em = Persistence.createEntityManagerFactory("track1").createEntityManager();
        configureFTS();
    }

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("ru.eventflow.nlp.xml");
        unmarshaller = jc.createUnmarshaller();

        em.getTransaction().begin();
        URL resoursesLocation = Runner.class.getResource("/annot.opcorpora.xml.byfile");
        for (File f : new File(resoursesLocation.getFile()).listFiles()) {
            if (!f.getName().endsWith(".xml")) continue;
            try {
                String id = f.getName().substring(0, f.getName().indexOf(".xml"));
                Document doc = processFile(id, f.getAbsolutePath());
                em.persist(doc);
            } catch (JAXBException e) {
                logger.info(f.getName());
            }
        }
        em.getTransaction().commit();


        String[] queries = new String[]{"книга", "президент предложил", "президенты предложили"};
        for (String q : queries) {
            List<Integer> ids = executeQuery(q);
            System.out.println(q + ": " + ids);
        }
    }

    private static void configureFTS() {
        em.getTransaction().begin();
        em.createNativeQuery("DROP TEXT SEARCH CONFIGURATION IF EXISTS public.fts_config;").executeUpdate();
        em.createNativeQuery("DROP TEXT SEARCH DICTIONARY IF EXISTS public.russian_stem;").executeUpdate();
        em.createNativeQuery("CREATE TEXT SEARCH CONFIGURATION public.fts_config ( COPY=pg_catalog.russian );").executeUpdate();
        em.createNativeQuery("CREATE TEXT SEARCH DICTIONARY public.russian_stem ( TEMPLATE=snowball, language='russian', stopwords='russian' );").executeUpdate();
        em.createNativeQuery("SET default_text_search_config = 'public.fts_config';").executeUpdate();
        em.getTransaction().commit();
    }

    private static Document processFile(String id, String filename) throws IOException, JAXBException {
        InputStream xml = new URL("file:///" + filename).openStream();
        JAXBElement<Text> document = unmarshaller.unmarshal(new StreamSource(xml), Text.class);
        xml.close();

        List<String> tags = document.getValue().getTags().getTag();
        String documentUrl = "";
        for (String t : tags) {
            if (t.startsWith("url:")) {
                documentUrl = t.substring(4);
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Paragraph paragraph : document.getValue().getParagraphs().getParagraph()) {
            for (Sentence sentence : paragraph.getSentence()) {
                sb.append(sentence.getSource()).append(" ");
            }
            sb.append('\n');
        }

        Document doc = new Document();
        doc.setUrl(documentUrl);
        doc.setText(sb.toString());
        doc.setId(new Integer(id));

        // flush to disk
        /*
        File fullTextFile = new File("/tmp/opcorpora/docs/" + id + ".txt");
        if (!fullTextFile.getParentFile().exists()) {
            fullTextFile.getParentFile().mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(fullTextFile);
        fout.write(sb.toString().getBytes());
        fout.close();
        */

        return doc;
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> executeQuery(String query) {
        Query q = em.createNativeQuery("SELECT id FROM document WHERE text @@ '" + query + "'", Integer.class);
        return (List<Integer>) q.getResultList();
    }
}
