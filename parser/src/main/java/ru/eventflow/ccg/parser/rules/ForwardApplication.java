package ru.eventflow.ccg.parser.rules;

import ru.eventflow.ccg.parser.SynCat;

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
    public SynCat apply() {
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
