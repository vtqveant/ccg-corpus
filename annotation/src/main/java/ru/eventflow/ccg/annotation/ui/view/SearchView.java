package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.JXTreeTable;
import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class SearchView extends JPanel {

    private static final SecondaryTableCellRenderer secondaryCellRenderer = new SecondaryTableCellRenderer();
    private final JTextField searchTextField;
    private final JButton searchBtn;
    private final JXTreeTable treeTable;

    public SearchView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 150));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchTextField = new JTextField();
        searchTextField.setEditable(true);
        searchTextField.setFont(Defaults.SMALL_FONT);
        searchPanel.add(searchTextField, BorderLayout.CENTER);

        ImageIcon searchIcon = new ImageIcon(ClassLoader.getSystemResource("images/magnify.gif"));
        searchBtn = new JButton(searchIcon);
        searchBtn.setFocusable(false);
        searchBtn.setSize(16, 16);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        add(searchPanel, BorderLayout.PAGE_START);

        LexiconTreeTableModel treeTableModel = new LexiconTreeTableModel();
        treeTable = new JXTreeTable(treeTableModel);
        treeTable.setFont(Defaults.SMALL_FONT);
        treeTable.getColumnModel().getColumn(1).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(2).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(3).setCellRenderer(secondaryCellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);
        treeTable.getTableHeader().setDefaultRenderer(headerRenderer);
        treeTable.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(100);
        treeTable.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(100);
        treeTable.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(45);

        treeTable.setTreeCellRenderer(new LexiconTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(treeTable);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTextField getSearchField() {
        return searchTextField;
    }

    public JButton getSearchBtn() {
        return searchBtn;
    }

    public JXTreeTable getTreeTable() {
        return treeTable;
    }

    private class LexiconTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            setFont(Defaults.SMALL_FONT);
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }

}
