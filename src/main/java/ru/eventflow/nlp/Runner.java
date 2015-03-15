package ru.eventflow.nlp;

import ru.eventflow.nlp.model.Document;
import ru.eventflow.nlp.xml.Paragraph;
import ru.eventflow.nlp.xml.Sentence;
import ru.eventflow.nlp.xml.Text;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

public class Runner {

    private static Unmarshaller unmarshaller;

    private static final Logger logger = Logger.getLogger("Runner");

    public static void main(String[] args) throws Exception {
        JAXBContext jc = JAXBContext.newInstance("ru.eventflow.nlp.xml");
        unmarshaller = jc.createUnmarshaller();

//        URL url = new URL("file:///home/transcend/code/NLU-RG/tracks/track1/data/annot.opcorpora.xml.byfile/" + FILENAME);

        EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();
        em.getTransaction().begin();

        File corpusDirectory = new File("/home/transcend/code/NLU-RG/tracks/track1/data/annot.opcorpora.xml.annot.opcorpora.xml.byfile");
        for (File f : corpusDirectory.listFiles()) {
            try {
                String id = f.getName().substring(0, f.getName().indexOf(".xml"));
                Document doc = processFile(id, f.getAbsolutePath());
                em.persist(doc);
            } catch (JAXBException e) {
                logger.info(f.getName());
            }
        }

        em.getTransaction().commit();

        // пример поискового запроса в PostgreSQL:
        // SELECT * FROM "document" WHERE "text" @@ 'юбилею' limit 10;

        // настройка текстовой конфигурации


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
        File fullTextFile = new File("/tmp/opcorpora/docs/" + id + ".txt");
        if (!fullTextFile.getParentFile().exists()) {
            fullTextFile.getParentFile().mkdirs();
        }
        FileOutputStream fout = new FileOutputStream(fullTextFile);
        fout.write(sb.toString().getBytes());
        fout.close();

        return doc;

    }
}
