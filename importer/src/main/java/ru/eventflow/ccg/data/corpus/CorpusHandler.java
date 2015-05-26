package ru.eventflow.ccg.data.corpus;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.eventflow.ccg.data.BaseNestedHandler;
import ru.eventflow.ccg.datasource.model.disambig.*;

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
                textHandler.text.setId(Integer.valueOf(attributes.getValue("id")));
                textHandler.text.setParent(Integer.valueOf(attributes.getValue("parent")));
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
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "paragraph":
                    ParagraphHandler paragraphHandler = new ParagraphHandler(reader, this);
                    paragraphHandler.paragraph.setId(Integer.valueOf(attributes.getValue("id")));
                    reader.setContentHandler(paragraphHandler);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws org.xml.sax.SAXException {
            switch (name) {
                case "tag":
                    text.addTag(content.toString());
                    break;
                case "text":
                    collector.addText(text);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class ParagraphHandler extends BaseNestedHandler {
        Paragraph paragraph = new Paragraph();

        public ParagraphHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "sentence":
                    SentenceHandler sentenceHandler = new SentenceHandler(reader, this);
                    sentenceHandler.sentence.setId(Integer.valueOf(attributes.getValue("id")));
                    reader.setContentHandler(sentenceHandler);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "paragraph":
                    ((TextHandler) parent).text.addParagraph(paragraph);
                    reader.setContentHandler(parent);
                    break;
            }
        }
    }

    class SentenceHandler extends BaseNestedHandler {
        Sentence sentence = new Sentence();

        public SentenceHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "token":
                    TokenHandler tokenHandler = new TokenHandler(reader, this);
                    tokenHandler.token.setId(Integer.valueOf(attributes.getValue("id")));
                    tokenHandler.token.setOrthography(attributes.getValue("text"));
                    reader.setContentHandler(tokenHandler);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "source":
                    sentence.setSource(content.toString());
                    break;
                case "sentence":
                    ((ParagraphHandler) parent).paragraph.addSentence(sentence);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class TokenHandler extends BaseNestedHandler {
        Token token = new Token();

        public TokenHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "tfr":
                    RevisionHandler revisionHandler = new RevisionHandler(reader, this);
                    revisionHandler.variant.setId(Integer.valueOf(attributes.getValue("rev_id")));
                    revisionHandler.variant.setLemma(attributes.getValue("t"));
                    reader.setContentHandler(revisionHandler);
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "token":
                    ((SentenceHandler) parent).sentence.addToken(token);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }

    class RevisionHandler extends BaseNestedHandler {

        Variant variant = new Variant();

        public RevisionHandler(XMLReader reader, ContentHandler parent) {
            super(reader, parent);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            content.setLength(0);
            switch (name) {
                case "l":
                    // I have keep these values because there's no information about the dictionary version in the dump
                    // lemma_id = 0 is Out of Vocabulary
                    variant.setLemmaId(Integer.valueOf(attributes.getValue("id"))); // XPath: //sentence/tokens/token/tfr/v/l/@id
                    variant.setLemma(attributes.getValue("t")); // XPath: //sentence/tokens/token/tfr/v/l/@t
                    break;
                case "g":
                    variant.addGrammeme(attributes.getValue("v")); // XPath: //sentence/tokens/token/tfr/v/l/g/@v
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            switch (name) {
                case "tfr":
                    ((TokenHandler) parent).token.addRevision(variant);
                    reader.setContentHandler(parent); // switch back to parent handler
                    break;
            }
        }
    }
}
