package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DictionaryParser {

    private List<Grammeme> grammemes;
    private List<Lexeme> lexemes;

    public DictionaryParser() {
    }

    public void process(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        DictionaryHandler handler = new DictionaryHandler(xmlReader);
        xmlReader.setContentHandler(handler);

        xmlReader.parse(new InputSource(in));

        grammemes = handler.grammemes;
        lexemes = handler.lexemes;
    }

    public List<Grammeme> getGrammemes() {
        return grammemes;
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }
}
