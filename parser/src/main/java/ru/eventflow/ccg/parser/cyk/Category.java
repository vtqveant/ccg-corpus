package ru.eventflow.ccg.parser.cyk;

public class Category {

    private Category left;
    private Category right;
    boolean forward; // either forward slash or backslash

    private String name;

    public Category(String name) {
        this.name = name.toLowerCase();
    }

    public Category(Category left, Category right, boolean forward) {
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

    public Category getLeft() {
        return left;
    }

    public Category getRight() {
        return right;
    }

    public String getName() {
        return name;
    }

    public void setLeft(Category left) {
        this.left = left;
    }

    public void setRight(Category right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (forward != category.forward) return false;
        if (left != null ? !left.equals(category.left) : category.left != null) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;
        if (right != null ? !right.equals(category.right) : category.right != null) return false;

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


