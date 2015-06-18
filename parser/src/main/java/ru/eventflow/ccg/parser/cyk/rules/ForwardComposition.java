package ru.eventflow.ccg.parser.cyk.rules;

import ru.eventflow.ccg.parser.cyk.Category;
import ru.eventflow.ccg.parser.cyk.CategoryBuilder;

/**
 * (>B)  X/Y  Y/Z  ->  X/Z
 */
public class ForwardComposition extends Rule {

    public final static Type TYPE = Type.FORWARD_COMPOSITION;

    private static ForwardComposition instance = new ForwardComposition();

    private ForwardComposition() {
    }

    public static ForwardComposition getInstance() {
        return instance;
    }

    @Override
    public Category apply() {
        if (left.getRight() != null && right.getRight() != null &&
                left.isForward() && right.isForward() && left.getRight().equals(right.getLeft())) {
            return CategoryBuilder.normalize(new Category(left.getLeft(), right.getRight(), true));
        }
        return null;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
