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
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Runner {

    public static final String FULL_DUMP = "annot.opcorpora.xml.byfile";
    public static final String GOLD_STANDARD = "gold";

    private static final Logger logger = Logger.getLogger("Runner");

    private static Unmarshaller unmarshaller;
    private static EntityManager em;

    private static final boolean FLUSH = false;

    static {
        em = Persistence.createEntityManagerFactory("track1").createEntityManager();
        configureFTS();
    }

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("ru.eventflow.nlp.xml");
        unmarshaller = jc.createUnmarshaller();

        URL resourcesLocation = Runner.class.getResource("/" + GOLD_STANDARD);
        List<File> files = scanResources(new File(resourcesLocation.getFile()));

        em.getTransaction().begin();
        for (File f : files) {
            Document doc;
            if (f.getName().endsWith(".xml")) {
                try {
                    doc = processXmlFile(f);
                } catch (JAXBException e) {
                    logger.info(f.getName());
                    continue;
                }
            } else if (f.getName().endsWith(".txt")) {
                doc = processTxtFile(f);
            } else {
                continue;
            }
            em.persist(doc);
            if (FLUSH) flushToDisk(doc);
        }
        em.getTransaction().commit();

        String[] queries = new String[]{"книга", "президент предложил", "президенты предложили"};
        for (String q : queries) {
            List<Integer> ids = executeQuery(q);
            System.out.println(q + ": " + ids);
        }
    }

    private static List<File> scanResources(File root) {
        return scanResources(new ArrayList<File>(), root);
    }

    private static List<File> scanResources(List<File> files, File root) {
        File[] children = root.listFiles();
        if (children != null) {
            for (File f : children) {
                if (f.isDirectory()) {
                    scanResources(files, f);
                } else {
                    files.add(f);
                }
            }
        }
        return files;
    }

    private static Document processTxtFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            sb.append(readData);
        }
        reader.close();

        String name = file.getName();
        return new Document(new Integer(name.substring(0, name.indexOf(".txt"))), null, sb.toString());
    }

    private static Document processXmlFile(File file) throws IOException, JAXBException {
        InputStream in = new URL("file:///" + file.getAbsolutePath()).openStream();
        JAXBElement<Text> document = unmarshaller.unmarshal(new StreamSource(in), Text.class);
        in.close();

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

        String name = file.getName();
        return new Document(new Integer(name.substring(0, name.indexOf(".xml"))), documentUrl, sb.toString());
    }

    private static void flushToDisk(Document doc) throws IOException {
        File fullTextFile = new File("/tmp/opcorpora/docs/" + doc.getId() + ".txt");
        if (!fullTextFile.getParentFile().exists()) {
            fullTextFile.getParentFile().mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(fullTextFile);
        fout.write(doc.getText().getBytes());
        fout.close();
    }

    private static void configureFTS() {
        em.getTransaction().begin();
        em.createNativeQuery("DROP TEXT SEARCH CONFIGURATION IF EXISTS public.fts_config;").executeUpdate();
        em.createNativeQuery("DROP TEXT SEARCH DICTIONARY IF EXISTS public.russian_stem;").executeUpdate();
        em.createNativeQuery("DROP INDEX IF EXISTS public.document_idx;").executeUpdate();
        em.createNativeQuery("CREATE TEXT SEARCH CONFIGURATION public.fts_config ( COPY=pg_catalog.russian );").executeUpdate();
        em.createNativeQuery("CREATE TEXT SEARCH DICTIONARY public.russian_stem ( TEMPLATE=snowball, language='russian', stopwords='russian' );").executeUpdate();
        em.createNativeQuery("CREATE INDEX document_idx ON public.document USING gin(to_tsvector('russian', text));").executeUpdate();
        em.createNativeQuery("SET default_text_search_config = 'public.fts_config';").executeUpdate();
        em.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> executeQuery(String query) {
        Query q = em.createNativeQuery("SELECT id FROM document WHERE text @@ '" + query + "'", Integer.class);
        return (List<Integer>) q.getResultList();
    }
}
