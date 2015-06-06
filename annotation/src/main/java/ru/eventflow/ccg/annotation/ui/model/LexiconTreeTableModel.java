package ru.eventflow.ccg.annotation.ui.model;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import java.util.ArrayList;

public class LexiconTreeTableModel extends AbstractTreeTableModel {
    private LexiconTreeNode root;

    public LexiconTreeTableModel() {
        root = new LexiconTreeNode(null, new ArrayList<String>(), -1);

            /*
            LexiconTreeNode form1 = new LexiconTreeNode("ноги", "нога", Arrays.asList("sg", "gen", "f"), 10);
            form1.getChildren().add(new SyntacticCategoryTreeNode("n", 5));
            form1.getChildren().add(new SyntacticCategoryTreeNode("n/n", 3));
            form1.getChildren().add(new SyntacticCategoryTreeNode("np", 2));
            root.getChildren().add(form1);

            LexiconTreeNode form2 = new LexiconTreeNode("ноги", "нога", Arrays.asList("pl", "nom", "f"), 15);
            form2.getChildren().add(new SyntacticCategoryTreeNode("n", 10));
            form2.getChildren().add(new SyntacticCategoryTreeNode("n/n", 5));
            root.getChildren().add(form2);
            */
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Syntactic Category";
            case 1:
                return "Form";
            case 2:
                return "Lemma";
            case 3:
                return "Count";
            default:
                return "Unknown";
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        LexiconTreeNode n = (LexiconTreeNode) node;
        if (column == 0) return n.toString();
        if (column == 1) return n.getForm().getOrthography();
        if (column == 2) return n.getLemma();
        if (column == 3) return n.getCount();
        return null;
    }

    @Override
    public Object getChild(Object node, int index) {
        LexiconTreeNode treenode = (LexiconTreeNode) node;
        return treenode.getChildren().get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        LexiconTreeNode treenode = (LexiconTreeNode) parent;
        return treenode.getChildren().size();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        LexiconTreeNode treenode = (LexiconTreeNode) parent;
        for (int i = 0; i > treenode.getChildren().size(); i++) {
            if (treenode.getChildren().get(i) == child) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((LexiconTreeNode) node).isLeaf();
    }

    @Override
    public Object getRoot() {
        return root;
    }
}
