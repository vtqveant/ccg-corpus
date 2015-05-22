package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.eventflow.ccg.datasource.model.dictionary.*;

/**
 * Uses a neat trick with switching handlers to parse child elements with a custom handler.
 */
public class DictionaryHandler extends DefaultHandler {

    private XMLReader reader;
    private DataCollector collector;

    public DictionaryHandler(XMLReader reader, DataCollector collector) {
        this.reader = reader;
        this.collector = collector;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        switch (name) {
            case "grammeme":
                GrammemeHandler grammemeHandler = new GrammemeHandler(reader, this);
                grammemeHandler.grammeme.setParent(collector.getGrammeme(attributes.getValue("parent")));
                reader.setContentHandler(grammemeHandler);
                break;
            case "lemma":
                LexemeHandler lexemeHandler = new LexemeHandler(reader, this);
                lexemeHandler.lexeme.setId(Integer.valueOf(attributes.getValue("id")));
                lexemeHandler.lexeme.setRev(Integer.valueOf(attributes.getValue("rev")));
                reader.setContentHandler(lexemeHandler);
                break;
            case "type":
                LinkTypeHandler linkTypeHandler = new LinkTypeHandler(reader, this);
                linkTypeHandler.linkType.setId(Integer.valueOf(attributes.getValue("id")));
                reader.setContentHandler(linkTypeHandler);
                break;
            case "link":
                Link link = new Link();
                link.setId(Integer.valueOf(attributes.getValue("id")));
                link.setFrom(collector.getLexeme(Integer.valueOf(attributes.getValue("from"))));
                link.setTo(collector.getLexeme(Integer.valueOf(attributes.getValue("to"))));
                link.setType(collector.getLinkType(Integer.valueOf(attributes.getValue("type"))));
                collector.addLink(link);
                break;
        }
    }

    abstract class BaseNestedHandler extends DefaultHandler {
        XMLReader reader;
        ContentHandler parent;
        StringBuilder content = new StringBuilder();

        protected BaseNestedHandler(XMLReader reader, ContentHandler parent) {
            this.reader = reader;
            this.parent = parent;
        }

        /**
         * characters can be called multiple times per element so aggregate the content in a StringBuilder
         */
        public void characters(char[] ch, int start, int length) throws SAXException {
            content.append(ch, start, length);
        }

        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
        }

        abstract public void endElement(String uri, String localName, String name) throws SAXException;
    }

    class GrammemeHandler extends BaseNestedHandler {
        Grammeme grammeme = new Grammeme();

        protected GrammemeHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "name":
                    grammeme.setName(content.toString());
                    break;
                case "alias":
                    grammeme.setAlias(content.toString());
                    break;
                case "description":
                    grammeme.setDescription(content.toString());
                    break;
                case "grammeme":
                    collector.addGrammeme(grammeme);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class LexemeHandler extends BaseNestedHandler {
        Lexeme lexeme = new Lexeme();

        protected LexemeHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            FormHandler formHandler = new FormHandler(reader, this);
            formHandler.form.setOrthography(attributes.getValue("t"));
            reader.setContentHandler(formHandler);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (name.equals("lemma")) {
                collector.addLexeme(lexeme);
                reader.setContentHandler(parent); // switch back to parent handler
            }
        }
    }

    class FormHandler extends BaseNestedHandler {
        Form form = new Form();

        public FormHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (name.equals("g")) {
                Grammeme g = collector.getGrammeme(attributes.getValue("v"));
                form.addGrammeme(g);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            LexemeHandler lexemeHandler = (LexemeHandler) parent;
            switch (name) {
                case "f":
                    form.setLemma(false);
                    form.setLexeme(lexemeHandler.lexeme);
                    lexemeHandler.lexeme.addForm(form);
                    reader.setContentHandler(lexemeHandler); // switch back to parent handler
                    break;
                case "l":
                    form.setLemma(true);
                    form.setLexeme(lexemeHandler.lexeme);
                    lexemeHandler.lexeme.setLemma(form);
                    reader.setContentHandler(lexemeHandler); // switch back to parent handler
                    break;
            }
        }
    }

    class LinkTypeHandler extends BaseNestedHandler {
        LinkType linkType = new LinkType();

        public LinkTypeHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (name.equals("type")) {
                linkType.setType(content.toString());
                collector.addLinkType(linkType);
                reader.setContentHandler(parent); // switch back to parent handler
            }
        }
    }

}
