package ru.eventflow.ccg.parser;

import java.util.Arrays;
import java.util.List;

public class CategoryBuilder {

    static List<String> terminals = Arrays.asList("s", "n", "np");

    public static Category build(String s) {
        return accept(new Category(s.toLowerCase()));
    }

    public static Category normalize(Category c) {
        return accept(c);
    }

    private static Category accept(Category s) {
        String name = s.getName();
        if (name.length() > 2) {
            Category only = accept(new Category(name.substring(1, name.length() - 1)));
            if (name.charAt(0) == '(' && only != null && name.charAt(name.length() - 1) == ')') {
                return only;
            }
        }
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/' || name.charAt(i) == '\\') {
                Category l = accept(new Category(name.substring(0, i)));
                Category r = accept(new Category(name.substring(i + 1)));
                if (l != null && r != null) {
                    s.setLeft(l);
                    s.setRight(r);
                    s.forward = (name.charAt(i) == '/');
                    return s;
                }
            }
        }
        if (terminals.contains(name)) {
            return s;
        }
        return null;
    }

}
