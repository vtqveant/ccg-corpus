package ru.eventflow.ccg.annotation.ui.model;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import ru.eventflow.ccg.datasource.model.corpus.Text;

public class CorpusTreeTableModel extends DefaultTreeTableModel {
    private final String[] columns = new String[]{"", "Id", "Sent.", "Words"};
    private final Class[] classes = new Class[]{Object.class, Integer.class, Integer.class, Integer.class};

    public CorpusTreeTableModel() {
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
        Text text = (Text) ((TreeTableNode) node).getUserObject();
        if (column == 1) return text.getId();
        if (column == 2) return 0;
        if (column == 3) return 0;
        return null;
    }

}
