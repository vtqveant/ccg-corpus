package ru.eventflow.ccg.parser;

import java.util.Arrays;
import java.util.List;

public class CategoryBuilder {

    private static List<String> terminals = Arrays.asList("s", "n", "np");

    public static SynCat build(String s) {
        return accept(new SynCat(s.toLowerCase()));
    }

    public static SynCat normalize(SynCat c) {
        return accept(c);
    }

    private static SynCat accept(SynCat s) {
        String name = s.getName();
        if (name.length() > 2) {
            SynCat only = accept(new SynCat(name.substring(1, name.length() - 1)));
            if (name.charAt(0) == '(' && only != null && name.charAt(name.length() - 1) == ')') {
                return only;
            }
        }
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '/' || name.charAt(i) == '\\') {
                SynCat l = accept(new SynCat(name.substring(0, i)));
                SynCat r = accept(new SynCat(name.substring(i + 1)));
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
