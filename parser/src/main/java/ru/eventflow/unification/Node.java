package ru.eventflow.unification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {
    private static int COUNTER = 0;
    private final int id;
    String label;
    Map<String, Node> features;
    Node clazz;

    public Node() {
        this.label = null;
        this.features = new HashMap<String, Node>();
        this.clazz = Node.this;
        this.id = COUNTER++;
    }

    public String getNominal() {
        return "n" + id;
    }

    @Override
    public String toString() {
        return toHL(new ArrayList<String>());
    }

    /**
     * @param accessibles to stop when a cycle is detected
     */
    protected String toHL(List<String> accessibles) {
        StringBuilder sb = new StringBuilder();
        sb.append('@');
        sb.append(getNominal());
        sb.append('(');
        if (label != null) sb.append(label);

        for (Map.Entry<String, Node> feature : features.entrySet()) {
            String nominal = feature.getValue().getNominal();

            sb.append('<');
            sb.append(feature.getKey().toLowerCase());
            sb.append(">(");
            sb.append(nominal);
            sb.append(") & ");

            // if not a loop
            if (!accessibles.contains(nominal) && !nominal.equals(getNominal())) {
                accessibles.add(nominal);
                sb.append(feature.getValue().toHL(accessibles));
                sb.append(" & ");
            }
        }
        // trim trailing ampersand
        if (features.size() > 0) {
            sb.delete(sb.length() - 3, sb.length());
        }
        sb.append(')');
        return sb.toString();
    }


}