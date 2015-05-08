package ru.eventflow.ccg.parser.rules;

import ru.eventflow.ccg.parser.Category;
import ru.eventflow.ccg.parser.CategoryBuilder;

/**
 * (>T)  X  ->  T/(T\X)
 */
public class ForwardTypeRaising extends Rule {

    public final static Type TYPE = Type.FORWARD_TYPE_RAISING;

    private static ForwardTypeRaising instance = new ForwardTypeRaising();

    private ForwardTypeRaising() {
    }

    public static ForwardTypeRaising getInstance() {
        return instance;
    }

    @Override
    public void setLeft(Category c1) {
        this.left = c1;
    }

    @Override
    public void setRight(Category c2) {
        assert false; // should not be called
    }

    @Override
    public Category apply() {
        Category s = new Category("s");
        return CategoryBuilder.normalize(new Category(s, new Category(s, left, false), true));
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
