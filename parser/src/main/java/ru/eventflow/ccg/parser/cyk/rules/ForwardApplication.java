package ru.eventflow.ccg.parser.cyk.rules;

import ru.eventflow.ccg.parser.cyk.Category;

/**
 * (>)  X/Y  Y  ->  X
 */
public class ForwardApplication extends Rule {

    public final static Type TYPE = Type.FORWARD_APPLICATION;

    private static ForwardApplication instance = new ForwardApplication();

    private ForwardApplication() {
    }

    public static ForwardApplication getInstance() {
        return instance;
    }

    @Override
    public Category apply() {
        if (left.getRight() != null && left.isForward() && left.getRight().equals(right)) {
            return left.getLeft();
        }
        return null;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
