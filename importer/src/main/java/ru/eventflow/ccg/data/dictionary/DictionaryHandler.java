package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * Uses a neat trick with switching handlers to parse child elements with a custom handler.
 */
public class DictionaryHandler extends DefaultHandler {

    private XMLReader reader;
    protected List<Grammeme> grammemes = new LinkedList<Grammeme>();
    protected List<Lexeme> lexemes = new LinkedList<Lexeme>();

    public DictionaryHandler(XMLReader reader) {
        this.reader = reader;
    }

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        switch (name) {
            case "grammeme":
                GrammemeHandler grammemeHandler = new GrammemeHandler(reader, this);
                grammemeHandler.grammeme.setParent(attributes.getValue("parent"));
                reader.setContentHandler(grammemeHandler);
                break;
            case "lemma":
                LemmaHandler lemmaHandler = new LemmaHandler(reader, this);
                lemmaHandler.lexeme.setId(Integer.valueOf(attributes.getValue("id")));
                lemmaHandler.lexeme.setRev(Integer.valueOf(attributes.getValue("rev")));
                reader.setContentHandler(lemmaHandler);
                break;
            case "type":
                // TODO
                break;
            case "link":
                // TODO
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
                    ((DictionaryHandler) parent).grammemes.add(grammeme);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class LemmaHandler extends BaseNestedHandler {
        Lexeme lexeme = new Lexeme();

        protected LemmaHandler(XMLReader reader, ContentHandler parent) {
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
                ((DictionaryHandler) parent).lexemes.add(lexeme);
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
                form.addGrammeme(attributes.getValue("v"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            LemmaHandler lemmaHandler = (LemmaHandler) parent;
            switch (name) {
                case "f":
                    lemmaHandler.lexeme.addForm(form);
                    reader.setContentHandler(lemmaHandler); // switch back to parent handler
                    break;
                case "l":
                    lemmaHandler.lexeme.setLemma(form);
                    reader.setContentHandler(lemmaHandler); // switch back to parent handler
                    break;
            }
        }
    }

}
