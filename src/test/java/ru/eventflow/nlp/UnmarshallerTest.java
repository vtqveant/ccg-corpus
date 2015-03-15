package ru.eventflow.nlp;

import org.junit.Test;
import ru.eventflow.nlp.xml.Text;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;

public class UnmarshallerTest {

    @Test
    public void testUnmarshaller() throws JAXBException, IOException {
        JAXBContext jc = JAXBContext.newInstance("ru.eventflow.nlp.xml");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        InputStream xml = getClass().getResourceAsStream("/opcorpora/1.xml");
        JAXBElement<Text> document = unmarshaller.unmarshal(new StreamSource(xml), Text.class);

        System.out.println(document.getValue().getName());
        for (String tag : document.getValue().getTags().getTag()) {
            System.out.println(tag);
        }

        xml.close();
    }
}
