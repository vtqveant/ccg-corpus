package ru.eventflow.ccgbank.data;


import ru.eventflow.annotation.xml.Paragraph;
import ru.eventflow.annotation.xml.Sentence;
import ru.eventflow.annotation.xml.Text;
import ru.eventflow.ccgbank.model.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
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
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Prepares a DB, preprocesses dumps and input texts
 */
public class CorpusDumpLoader {

    private static final Logger logger = Logger.getLogger(CorpusDumpLoader.class.getName());
    private static final EntityManager entityManager = Persistence.createEntityManagerFactory("h2-openjpa").createEntityManager();
    private static Unmarshaller unmarshaller;

    public CorpusDumpLoader() {
        try {
            JAXBContext jc = JAXBContext.newInstance("ru.eventflow.annotation.xml");
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException e) {
            logger.severe("Initialization failed");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Integer> init(String resourcesLocation) {
        List<Integer> documentIds = new ArrayList<>();
        try {
            File resourcesLocationFile = new File(resourcesLocation);
            if (!resourcesLocationFile.exists()) {
                logger.severe("Unable to load corpus data, " + resourcesLocation + " does not exist");
                System.exit(1);
            }
            List<File> files = scanResources(resourcesLocationFile);
            logger.info(files.size() + " documents in corpus");

            entityManager.getTransaction().begin();
            for (File f : files) {
                Document doc;
                if (f.getName().endsWith(".xml")) {
                    try {
                        doc = processXmlFile(f);
                    } catch (JAXBException e) {
                        logger.warning(f.getName());
                        continue;
                    }
                } else {
                    continue;
                }
                entityManager.persist(doc);
                documentIds.add(doc.getId());
            }
            entityManager.getTransaction().commit();
            logger.info(documentIds.size() + " documents processed");
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

}
