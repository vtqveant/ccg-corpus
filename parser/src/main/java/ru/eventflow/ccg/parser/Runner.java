package ru.eventflow.ccg.parser;

import java.util.List;
import java.util.Set;

public class Runner {

    private static final String[] sentences = new String[]{
            "John loves Mary", // 6 parses for Steedman's version of CCG (>, <, >T, <T, >B, <B)
            "man that Bob saw", // 1 parse, from (White and Baldridge, Adapting Chart realization to CCG), results in N
            "man and woman",
            "Mary and John",
            "John and Mary love Bob and Mary loves John and Peter"
    };

    public static void main(String[] args) {
        for (String sentence : sentences) {
            List<Tok> tokens = Utils.tokenize(sentence);
            Set<Entry> lexicon = LexiconBuilder.getLexicon();
            List<Item> parses = new Parser(lexicon).parse(tokens);

            System.out.println(sentence);
            System.out.println(parses.size() + " parse(s)");
            Utils.print(parses);
        }
    }
}
