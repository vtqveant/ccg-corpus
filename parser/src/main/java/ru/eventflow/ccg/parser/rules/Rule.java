package ru.eventflow.ccg.parser.rules;

import ru.eventflow.ccg.parser.Category;

public abstract class Rule {

    public enum Type {

        FORWARD_APPLICATION(">"),
        BACKWARD_APPLICATION("<"),
        FORWARD_COMPOSITION(">B"),
        BACKWARD_COMPOSITION("<B"),
        FORWARD_TYPE_RAISING(">T"),
        BACKWARD_TYPE_RAISING("<T");

        private String abbreviation;

        Type(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String getAbbreviation() {
            return abbreviation;
        }
    }

    protected Category left;
    protected Category right;

    public void setLeft(Category left) {
        this.left = left;
    }

    public void setRight(Category right) {
        this.right = right;
    }

    public abstract Category apply();

    public abstract Type getType();

}
