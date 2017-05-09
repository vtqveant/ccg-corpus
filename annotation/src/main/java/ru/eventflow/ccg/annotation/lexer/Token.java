package ru.eventflow.ccg.annotation.lexer;

public abstract class Token {

    int id;
    int start;
    int end;

    public Token(int id, int start, int end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    /**
     * Check if the token id starts with val, used to assign syntax highlighting style to the token based on its id
     */
    boolean check(int val) {
        return ((id >> 8) == val);
    }

    public abstract ColorScheme.Style getStyle();

    public int getId() {
        return id;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return ("Token #" + Integer.toHexString(id) + " [" + start + ", " + end + "]");
    }

}
