package ru.eventflow.unification;

import java.util.Map;
import java.util.Stack;

/**
 * s. Francez, Wintner. Unification Grammars. 2012, p. 94f.
 * <p>
 * This is a destructive algorithm, make sure the nodes are copied if you want to reuse them.
 */
public class Unification {

    public static final Node CONTRADICTION = null;

    /**
     * receives a node and returns a unique canonical representative of its equivalence class
     */
    public static Node find(Node node) {
        if (node.clazz == node) return node;
        return find(node.clazz);
    }

    public static boolean isLabeled(Node node) {
        return node.label != null;
    }

    public static boolean isSink(Node node) {
        return node.features.size() == 0;
    }

    /**
     * Given two feature graphs A and B, if fs(A) and fs(B) are unifiable, returns
     * a representative of $fs(A) \sqcup fs(B)$, otherwise fail.
     */
    public static Node unify(Node n1, Node n2) {
        // unifying anything with \bot results in \bot
        if (n1 == CONTRADICTION || n2 == CONTRADICTION) {
            return CONTRADICTION;
        }

        final Stack<Pair> S = new Stack<>();
        S.push(new Pair(n1, n2));

        while (S.size() > 0) {
            Pair pair = S.pop();
            Node q1 = find(pair.q1);
            Node q2 = find(pair.q2);
            if (q1 != q2) {
                if ((isLabeled(q1) && !isSink(q2)) || (isLabeled(q2) && !isSink(q1)) ||
                        (isLabeled(q1) && isLabeled(q2) && !q1.label.equals(q2.label))) {
                    return CONTRADICTION; // unification failure
                } else {
                    // merge two equivalence classes of two nodes by setting the equiv. class of all
                    // members of the second class to that of the first
                    q2.clazz = q1.clazz;

                    // fix pointers
                    for (Map.Entry<String, Node> entry : q1.features.entrySet()) {
                        if (entry.getValue() == q2) {
                            entry.setValue(q1.clazz);
                        }
                    }

                    if (isSink(q1) && isSink(q2) && isLabeled(q2)) {
                        q1.label = q2.label;
                    }

                    // iterate over the features of the second node and add the links to the first one
                    for (String f : q2.features.keySet()) {
                        if (q1.features.containsKey(f)) {
                            S.push(new Pair(q1.features.get(f), q2.features.get(f)));
                        } else {
                            q1.features.put(f, find(q2.features.get(f)));
                        }
                    }
                }
            }
        }

        return find(n1);
    }

    private static class Pair {
        Node q1;
        Node q2;

        public Pair(Node q1, Node q2) {
            this.q1 = q1;
            this.q2 = q2;
        }
    }

}
