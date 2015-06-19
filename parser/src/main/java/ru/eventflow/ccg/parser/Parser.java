package ru.eventflow.ccg.parser;

import ru.eventflow.ccg.parser.rules.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * CYK parser for CCG
 */
public class Parser {

    Set<Entry> lexicon;

    /**
     * rule set is fixed for CCG, type raising is used separately
     */
    private Rule[] rules = new Rule[]{
            ForwardApplication.getInstance(), BackwardApplication.getInstance(),
            ForwardComposition.getInstance(), BackwardComposition.getInstance()
    };
    private Rule[] typeRaisingRules = new Rule[]{
            ForwardTypeRaising.getInstance(), BackwardTypeRaising.getInstance()
    };

    /**
     * any simple type is acceptable for the parser
     */
    private final List<String> acceptable = Arrays.asList("s", "n", "np");

    public Parser(Set<Entry> lexicon) {
        this.lexicon = lexicon;
    }

    public List<Item> parse(List<Tok> tokens) {

        // create chart
        int n = tokens.size();
        List<List<List<Item>>> chart = new ArrayList<List<List<Item>>>(n);
        for (int idx = 0; idx < n; idx++) {              // left edge
            chart.add(idx, new ArrayList<List<Item>>());
            for (int span = 0; span < n - idx; span++) { // span size
                chart.get(idx).add(span, new ArrayList<Item>());
            }
        }

        // scan phase
        for (int idx = 0; idx < n; idx++) {
            Tok token = tokens.get(idx);
            for (Entry entry : lexicon) {
                if (entry.getOrthography().equals(token.getValue())) {
                    chart.get(idx).get(0).add(new Item(entry.getSynCat(), idx, idx + 1));
                }
            }
        }

        // complete phase
        for (int span = 0; span < n; span++) {   // span size
            for (int idx = 0; idx < n; idx++) {  // left edge
                if (idx + span >= n) continue;

                for (Rule rule : rules) {

                    List<Item> newItems = new ArrayList<Item>();
                    for (Item item : chart.get(idx).get(span)) {
                        // type raising rules are applied to simple categories only
                        if (item.left == null && item.right == null) {
                            for (Rule rr : typeRaisingRules) {
                                rr.setLeft(item.synCat);
                                Item p = new Item(rr.apply(), idx, idx + span, item, null, rr.getType());
                                if (!chart.get(idx).get(span).contains(p)) {
                                    newItems.add(p);
                                }
                            }
                        }
                    }
                    chart.get(idx).get(span).addAll(newItems);

                    for (int splitOffset = 1; splitOffset <= span; splitOffset++) {
                        for (Item left : chart.get(idx).get(splitOffset - 1)) {
                            for (Item right : chart.get(idx + splitOffset).get(span - splitOffset)) {
                                rule.setLeft(left.synCat);
                                rule.setRight(right.synCat);
                                SynCat reduced = rule.apply();
                                if (reduced != null) {
                                    Item p = new Item(reduced, idx, idx + span, left, right, rule.getType());
                                    if (!chart.get(idx).get(span).contains(p)) {
                                        chart.get(idx).get(span).add(p);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // accept and backtrack
        List<Item> parses = new ArrayList<Item>();
        for (Item item : chart.get(0).get(tokens.size() - 1)) {
            if (acceptable.contains(item.synCat.toString())) {
                parses.add(item);
            }
        }
        return parses;
    }
}
