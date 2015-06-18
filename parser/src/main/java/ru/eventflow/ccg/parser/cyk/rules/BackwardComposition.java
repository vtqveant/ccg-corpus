package ru.eventflow.ccg.parser.cyk.rules;

import ru.eventflow.ccg.parser.cyk.Category;
import ru.eventflow.ccg.parser.cyk.CategoryBuilder;

/**
 * (<B)  Y\Z  X\Y  ->  X\Z
 */
public class BackwardComposition extends Rule {

    public final static Type TYPE = Type.BACKWARD_COMPOSITION;

    private static BackwardComposition instance = new BackwardComposition();

    private BackwardComposition() {
    }

    public static BackwardComposition getInstance() {
        return instance;
    }

    @Override
    public Category apply() {
        if (left.getRight() != null && right.getRight() != null &&
                left.isForward() && right.isForward() && left.getLeft().equals(right.getRight())) {
            return CategoryBuilder.normalize(new Category(right.getLeft(), left.getRight(), false));
        }
        return null;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
