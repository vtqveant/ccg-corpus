package ru.eventflow.ccg.parser.cyk.rules;

import ru.eventflow.ccg.parser.cyk.Category;
import ru.eventflow.ccg.parser.cyk.CategoryBuilder;

/**
 * (<T)  X ->  T\(T/X)
 */
public class BackwardTypeRaising extends Rule {

    public final static Type TYPE = Type.BACKWARD_TYPE_RAISING;

    private static BackwardTypeRaising instance = new BackwardTypeRaising();

    private BackwardTypeRaising() {
    }

    public static BackwardTypeRaising getInstance() {
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
        return CategoryBuilder.normalize(new Category(s, new Category(s, left, true), false));
    }

    @Override
    public Type getType() {
        return TYPE;
    }
}
