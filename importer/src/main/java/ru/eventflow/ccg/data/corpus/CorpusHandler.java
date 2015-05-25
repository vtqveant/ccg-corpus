package ru.eventflow.ccg.data.corpus;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.eventflow.ccg.data.BaseNestedHandler;
import ru.eventflow.ccg.data.xml.annot.Text;

import java.math.BigDecimal;

public class CorpusHandler extends DefaultHandler {

    private XMLReader reader;
    private CorpusDataCollector collector;

    public CorpusHandler(XMLReader reader, CorpusDataCollector collector) {
        this.reader = reader;
        this.collector = collector;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        switch (name) {
            case "annotation":
                collector.revision = attributes.getValue("revision");
                collector.version = attributes.getValue("version");
                break;
            case "text":
                TextHandler textHandler = new TextHandler(reader, this);
                textHandler.text.setId(new BigDecimal(attributes.getValue("id")));
                textHandler.text.setParent(new BigDecimal(attributes.getValue("parent")));
                textHandler.text.setName(attributes.getValue("name"));
                reader.setContentHandler(textHandler);
                break;
        }
    }

    class TextHandler extends BaseNestedHandler {
        Text text = new Text();

        protected TextHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws org.xml.sax.SAXException {
            switch (name) {
                case "tag":
                    //text.getTags().getTag().add(content.toString());
                    break;
                case "text":
                    collector.addText(text);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }
}
