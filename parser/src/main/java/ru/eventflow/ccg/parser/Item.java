package ru.eventflow.ccg.parser;

import ru.eventflow.ccg.parser.rules.Rule;

public class Item {
    Category category;
    int start;
    int end;
    Item left;
    Item right;

    /**
     * The type of the rule used to produce this item.
     * Must not be included in equals()
     */
    private Rule.Type type;

    public Item(Category category, int start, int end) {
        this.category = category;
        this.start = start;
        this.end = end;
    }

    public Item(Category category, int start, int end, Item left, Item right, Rule.Type type) {
        this.category = category;
        this.start = start;
        this.end = end;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (end != item.end) return false;
        if (start != item.start) return false;
        if (category != null ? !category.equals(item.category) : item.category != null) return false;
        if (left != null ? !left.equals(item.left) : item.left != null) return false;
        if (right != null ? !right.equals(item.right) : item.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = category != null ? category.hashCode() : 0;
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
