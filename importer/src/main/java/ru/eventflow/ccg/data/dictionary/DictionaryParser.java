package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class DictionaryParser {

    private DataBridge bridge;

    public DictionaryParser(DataBridge bridge) {
        this.bridge = bridge;
    }

    public void process(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        DictionaryHandler handler = new DictionaryHandler(xmlReader, bridge);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(in));
    }

    public List<Grammeme> getGrammemes() {
        return bridge.getGrammemes();
    }

    public List<Lexeme> getLexemes() {
        return bridge.getLexemes();
    }
}
