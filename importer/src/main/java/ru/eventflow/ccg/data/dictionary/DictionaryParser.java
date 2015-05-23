package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class DictionaryParser {

    private DictionaryDataCollector collector;

    public DictionaryParser(DictionaryDataCollector collector) {
        this.collector = collector;
    }

    public void process(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        DictionaryHandler handler = new DictionaryHandler(xmlReader, collector);
        xmlReader.setContentHandler(handler);
        xmlReader.parse(new InputSource(in));
    }
}
