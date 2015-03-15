package ru.eventflow.nlp;

import ru.eventflow.nlp.model.Document;
import ru.eventflow.nlp.xml.Paragraph;
import ru.eventflow.nlp.xml.Sentence;
import ru.eventflow.nlp.xml.Text;

import javax.persistence.EntityManager;
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

/**
 * Prepares a DB, preprocesses dumps and input texts
 */
public class SourceProcessor {

    private String resourcesLocation;

    private static final Logger logger = Logger.getLogger(SourceProcessor.class.getName());

    private Unmarshaller unmarshaller;
    private EntityManager em;

    private boolean flush;

    public SourceProcessor(EntityManager em, String resourcesLocation, boolean flush) {
        this.em = em;
        this.resourcesLocation = resourcesLocation;
        this.flush = flush;
        try {
            init();
        } catch (Exception e) {
            logger.severe("Initialization failed");
            System.exit(1);
        }
    }

    private void init() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance("ru.eventflow.nlp.xml");
        unmarshaller = jc.createUnmarshaller();

        List<File> files = scanResources(new File(resourcesLocation));

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
            if (flush) flushToDisk(doc);
        }
        em.getTransaction().commit();
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

}
