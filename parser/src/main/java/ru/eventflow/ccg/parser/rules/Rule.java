package ru.eventflow.ccg.parser.rules;

import ru.eventflow.ccg.parser.SynCat;

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

    protected SynCat left;
    protected SynCat right;

    public void setLeft(SynCat left) {
        this.left = left;
    }

    public void setRight(SynCat right) {
        this.right = right;
    }

    public abstract SynCat apply();

    public abstract Type getType();

}
