package ru.eventflow.ccg.data;

import ru.eventflow.ccg.data.xml.annot.Paragraph;
import ru.eventflow.ccg.data.xml.annot.Sentence;
import ru.eventflow.ccg.data.xml.annot.Text;
import ru.eventflow.ccg.datasource.DataSource;
import ru.eventflow.ccg.datasource.model.corpus.Document;

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

public class DumpLoader {

    private static final Logger logger = Logger.getLogger(DumpLoader.class.getName());
    private static Unmarshaller unmarshaller;
    private File resourcesLocationFile;

    private static final EntityManager entityManager = Persistence.createEntityManagerFactory(DataSource.DEFAULT).createEntityManager();

    public static void main(String[] args) {
        if (args.length == 0 || args.length % 2 != 0 || !args[0].equals("--resources")) {
            System.out.println("Exit due to misconfiguration");
            System.exit(-1);
        }
        String resourcesLocation = args[1];
        new DumpLoader(resourcesLocation).init();
    }

    public DumpLoader(String resourcesLocation) {
        resourcesLocationFile = new File(resourcesLocation);
        if (!resourcesLocationFile.exists()) {
            logger.severe("Unable to load corpus data, " + resourcesLocation + " does not exist");
            System.exit(1);
        }

        try {
            JAXBContext jc = JAXBContext.newInstance("ru.eventflow.ccg.data.xml.annot");
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
            List<File> files = scanResources(resourcesLocationFile);
            logger.info(files.size() + " documents in corpus");

            entityManager.getTransaction().begin();
            for (File f : files) {
                Document doc;
                if (f.getName().endsWith(".xml")) {
                    try {
                        doc = processXmlFile(f);
                        entityManager.persist(doc);
                        documentIds.add(doc.getId());
                    } catch (JAXBException e) {
                        logger.warning(f.getName());
                    }
                }
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
        return processXmlFile(in);
    }

    private Document processXmlFile(InputStream in) throws IOException, JAXBException {
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
                sb.append(sentence.getSource()).append(' ');
            }
            sb.append('\n');
        }

        int id = document.getValue().getId().intValue();
        int parentId = document.getValue().getParent().intValue();
        String name = document.getValue().getName();
        return new Document(id, name, parentId, documentUrl, sb.toString());
    }

}
