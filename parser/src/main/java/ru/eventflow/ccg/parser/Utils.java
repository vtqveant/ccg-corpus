package ru.eventflow.ccg.parser;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static void print(List<Item> items) {
        for (Item item : items) {
            System.out.println(getTree(item));
        }
        System.out.println();
    }

    public static List<Tok> tokenize(String sentence) {
        List<Tok> tokens = new ArrayList<Tok>();
        for (String part : sentence.toLowerCase().split("\\s+")) {
            tokens.add(new Tok(part));
        }
        return tokens;
    }

    public static StringBuilder getTree(Item item, StringBuilder prefix, boolean tail, StringBuilder sb) {
        if (item.right != null) {
            getTree(item.right, new StringBuilder().append(prefix).append(tail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(tail ? "└── " : "┌── ").append(item.synCat).append("\n");
        if (item.left != null) {
            getTree(item.left, new StringBuilder().append(prefix).append(tail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    public static String getTree(Item item) {
        return getTree(item, new StringBuilder(), true, new StringBuilder()).toString();
    }


}
