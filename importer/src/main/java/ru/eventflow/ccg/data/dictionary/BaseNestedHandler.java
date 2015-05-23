package ru.eventflow.ccg.data.dictionary;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

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