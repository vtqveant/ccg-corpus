package ru.eventflow.unification;

import java.util.HashMap;
import java.util.Map;

public class Node {
    String label;
    Map<String, Node> features;
    Node clazz;

    public Node() {
        this.label = null;
        this.features = new HashMap<String, Node>();
        this.clazz = Node.this;
    }

    @Override
    public String toString() {
        if (label != null) return label;

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (String name : features.keySet()) {
            sb.append(name);
        }
        sb.append("]");
        return sb.toString();
    }


}