package ru.eventflow.ccg.annotation.ui.model;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class LexiconTreeTableModel extends DefaultTreeTableModel {
    private static final String[] columns = new String[]{" Category", "Lemma", "Count"};
    private static final Class[] classes = new Class[]{Object.class, String.class, String.class, Integer.class};

    public LexiconTreeTableModel() {
        root = new DefaultMutableTreeTableNode();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return classes[column];
    }

    @Override
    public Object getValueAt(Object node, int column) {
        TreeTableNode treeTableNode = (TreeTableNode) node;
        AbstractLexiconEntry entry = (AbstractLexiconEntry) treeTableNode.getUserObject();
        if (column == 1) return entry.getLemma();
        if (column == 2) return entry.getCount();
        return "";
    }
}
