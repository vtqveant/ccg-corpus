package ru.eventflow.ccg.data.dictionary;

import ru.eventflow.ccg.datasource.model.dictionary.Grammeme;
import ru.eventflow.ccg.datasource.model.dictionary.Lexeme;
import ru.eventflow.ccg.datasource.model.dictionary.Link;
import ru.eventflow.ccg.datasource.model.dictionary.LinkType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollector {

    private Map<String, Grammeme> grammemes = new HashMap<>();
    private Map<Integer, Lexeme> lexemes = new HashMap<>();
    private Map<Integer, LinkType> linkTypes = new HashMap<>();
    private Map<Integer, Link> links = new HashMap<>();

    public DataCollector() {
    }

    public void addGrammeme(Grammeme grammeme) {
        grammemes.put(grammeme.getName(), grammeme);
    }

    public void addLexeme(Lexeme lexeme) {
        lexemes.put(lexeme.getId(), lexeme);
    }

    public void addLinkType(LinkType type) {
        linkTypes.put(type.getId(), type);
    }

    public void addLink(Link link) {
        links.put(link.getId(), link);
    }

    public Grammeme getGrammeme(String name) {
        return grammemes.get(name);
    }

    public Lexeme getLexeme(int id) {
        return lexemes.get(id);
    }

    public LinkType getLinkType(int id) {
        return linkTypes.get(id);
    }

    public List<LinkType> getLinkTypes() {
        List<LinkType> l = new ArrayList<>();
        l.addAll(linkTypes.values());
        return l;
    }

    public List<Grammeme> getGrammemes() {
        List<Grammeme> l = new ArrayList<>();
        l.addAll(grammemes.values());
        return l;
    }

    public List<Lexeme> getLexemes() {
        List<Lexeme> l = new ArrayList<>();
        l.addAll(lexemes.values());
        return l;
    }

    public List<Link> getLinks() {
        List<Link> l = new ArrayList<>();
        l.addAll(links.values());
        return l;
    }
}
