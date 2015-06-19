package ru.eventflow.ccg.parser;

/**
 * Syntactic category
 */
public class SynCat {

    private SynCat left;
    private SynCat right;
    boolean forward; // either forward slash or backslash

    private String name;

    public SynCat(String name) {
        this.name = name.toLowerCase();
    }

    public SynCat(SynCat left, SynCat right, boolean forward) {
        this.left = left;
        this.right = right;
        this.forward = forward;
        this.name = toString();
    }

    public boolean isForward() {
        return forward;
    }

    // for convenience
    public boolean isBackward() {
        return !forward;
    }

    public SynCat getLeft() {
        return left;
    }

    public SynCat getRight() {
        return right;
    }

    public String getName() {
        return name;
    }

    public void setLeft(SynCat left) {
        this.left = left;
    }

    public void setRight(SynCat right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynCat)) return false;

        SynCat synCat = (SynCat) o;

        if (forward != synCat.forward) return false;
        if (left != null ? !left.equals(synCat.left) : synCat.left != null) return false;
        if (name != null ? !name.equals(synCat.name) : synCat.name != null) return false;
        if (right != null ? !right.equals(synCat.right) : synCat.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (forward ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (left == null && right == null) return name;
        return "(" + left + (forward ? "/" : "\\") + right + ")";
    }
}


