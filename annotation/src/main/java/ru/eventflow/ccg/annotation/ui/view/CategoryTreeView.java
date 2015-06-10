package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.CustomTreeCellRenderer;
import ru.eventflow.ccg.annotation.ui.component.SecondaryTableCellRenderer;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeNode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class CategoryTreeView extends JPanel implements SearchEnabled {

    private static final SecondaryTableCellRenderer secondaryCellRenderer = new SecondaryTableCellRenderer();
    private final JXTreeTable treeTable;

    public CategoryTreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 150));

        LexiconTreeTableModel treeTableModel = new LexiconTreeTableModel();
        treeTable = new JXTreeTable(treeTableModel);
        treeTable.setFont(Defaults.SMALL_FONT);
        treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.getColumnModel().getColumn(1).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(2).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(3).setCellRenderer(secondaryCellRenderer);

        DefaultTreeRenderer treeCellRenderer = new CustomTreeCellRenderer();
        treeTable.setTreeCellRenderer(treeCellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);

        JTableHeader tableHeader = treeTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setDefaultRenderer(headerRenderer);
        tableHeader.getColumnModel().getColumn(1).setMaxWidth(100);
        tableHeader.getColumnModel().getColumn(2).setMaxWidth(100);
        tableHeader.getColumnModel().getColumn(3).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(treeTable);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void addSearchPanel(SearchView searchPanel) {
        add(searchPanel, BorderLayout.PAGE_START);
    }

    public JXTreeTable getTreeTable() {
        return treeTable;
    }

    public class LexiconTreeTableModel extends AbstractTreeTableModel {
        private final String[] columns = new String[]{" Category", "Form", "Lemma", "Count"};

        public LexiconTreeTableModel() {
            root = new LexiconTreeNode(null, new ArrayList<String>(), -1);
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
        public Object getValueAt(Object node, int column) {
            LexiconTreeNode n = (LexiconTreeNode) node;
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
    }

}
