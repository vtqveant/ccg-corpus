package ru.eventflow.fts;

import ru.eventflow.fts.datasource.Document;
import ru.eventflow.fts.xml.Paragraph;
import ru.eventflow.fts.xml.Sentence;
import ru.eventflow.fts.xml.Text;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

/**
 * Prepares a DB, preprocesses dumps and input texts
 */
class CorpusDataPreprocessor {

    private String resourcesLocation;

    private static final Logger logger = Logger.getLogger(CorpusDataPreprocessor.class.getName());

    private Unmarshaller unmarshaller;
    private EntityManager em;

    private boolean flush;

    public CorpusDataPreprocessor(EntityManager em, String resourcesLocation, boolean flush) {
        this.em = em;
        this.resourcesLocation = resourcesLocation;
        this.flush = flush;

        try {
            JAXBContext jc = JAXBContext.newInstance("ru.eventflow.fts.xml");
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException e) {
            logger.severe("Initialization failed");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Integer> init() {
        List<Integer> documentIds = new ArrayList<>();
        try {
            List<File> files = scanResources(new File(resourcesLocation));
            logger.info(files.size() + " documents in corpus");

            em.getTransaction().begin();
            for (File f : files) {
                Document doc;
                if (f.getName().endsWith(".xml")) {
                    try {
                        doc = processXmlFile(f);
                    } catch (JAXBException e) {
                        logger.warning(f.getName());
                        continue;
                    }
                } else if (f.getName().endsWith(".txt")) {
                    doc = processTxtFile(f);
                } else {
                    continue;
                }
                em.persist(doc);
                documentIds.add(doc.getId());
                if (flush) flushToDisk(doc);
            }
            em.getTransaction().commit();
            logger.info(documentIds.size() + " documents processed");

            configureFTS();
            logger.info("FTS configuration done");
        } catch (IOException e) {
            logger.severe("Initialization failed");
            e.printStackTrace();
            System.exit(1);
        }

        Collections.sort(documentIds);
        return documentIds;
    }

    private List<File> scanResources(File root) {
        return scanResources(new ArrayList<File>(), root);
    }

    private List<File> scanResources(List<File> files, File root) {
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

    private Document processTxtFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        StringBuilder sb = new StringBuilder();
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            sb.append(readData);
        }
        reader.close();

        String name = file.getName();
        return new Document(new Integer(name.substring(0, name.indexOf(".txt"))), null, sb.toString());
    }

    private Document processXmlFile(File file) throws IOException, JAXBException {
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

    private void flushToDisk(Document doc) throws IOException {
        File fullTextFile = new File("/tmp/opcorpora/docs/" + doc.getId() + ".txt");
        if (!fullTextFile.getParentFile().exists()) {
            fullTextFile.getParentFile().mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(fullTextFile);
        fout.write(doc.getText().getBytes());
        fout.close();
    }

    private void configureFTS() {
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

}
