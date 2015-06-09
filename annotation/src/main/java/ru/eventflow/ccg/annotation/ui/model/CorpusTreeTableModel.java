package ru.eventflow.ccg.annotation.ui.model;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CorpusTreeTableModel extends AbstractTreeTableModel {
    private final String[] columns = new String[]{"", "Id", "Sent.", "Words"};
    private final Class[] classes = new Class[]{Object.class, Integer.class, Integer.class, Integer.class};

    public CorpusTreeTableModel() {
        root = new DefaultMutableTreeNode("OpenCorpora");
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

    // JXTreeTable ignores the value at column = 0, because it is rendered with TreeCellRenderer
    @Override
    public Object getValueAt(Object node, int column) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) node;
        Text text = (Text) n.getUserObject();
        if (column == 1) return text.getId();
        if (column == 2) return 0;
        if (column == 3) return 0;
        return null;
    }

    @Override
    public Object getChild(Object node, int index) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) node;
        return n.getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) parent;
        return n.getChildCount();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) parent;
        return n.getIndex((TreeNode) child);
    }

}
