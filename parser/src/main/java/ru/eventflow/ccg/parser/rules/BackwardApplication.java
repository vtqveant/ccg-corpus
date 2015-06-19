package ru.eventflow.ccg.parser.rules;

import ru.eventflow.ccg.parser.SynCat;

/**
 * (>)  Y  X\Y  ->  X
 */
public class BackwardApplication extends Rule {

    public final static Type TYPE = Type.BACKWARD_APPLICATION;

    private static BackwardApplication instance = new BackwardApplication();

    private BackwardApplication() {
    }

    public static BackwardApplication getInstance() {
        return instance;
    }

    @Override
    public SynCat apply() {
        if (right.getRight() != null && right.isBackward() && right.getRight().equals(left)) {
            return right.getLeft();
        }
        return null;
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
