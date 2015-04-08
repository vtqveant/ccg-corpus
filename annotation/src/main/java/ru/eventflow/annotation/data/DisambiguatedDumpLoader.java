package ru.eventflow.annotation.data;

import ru.eventflow.annotation.model.Document;
import ru.eventflow.annotation.xml.Annotation;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

public class DisambiguatedDumpLoader {

    private static final Logger logger = Logger.getLogger(CorpusDumpLoader.class.getName());

    private Unmarshaller unmarshaller;
    private EntityManager entityManager;

    public DisambiguatedDumpLoader() {
        this.entityManager = DataManager.getEnitityManager();
        try {
            JAXBContext jc = JAXBContext.newInstance("ru.eventflow.annotation.xml");
            unmarshaller = jc.createUnmarshaller();
        } catch (JAXBException e) {
            logger.severe("Initialization failed");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void init(String resourceLocation) {
        try {
            File dump = new File(resourceLocation);
            processXmlFile(dump);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document processXmlFile(File file) throws IOException, JAXBException {
        InputStream in = new URL("file:///" + file.getAbsolutePath()).openStream();
        JAXBElement<Annotation> document = unmarshaller.unmarshal(new StreamSource(in), Annotation.class);
        in.close();

        return null;
    }
}
