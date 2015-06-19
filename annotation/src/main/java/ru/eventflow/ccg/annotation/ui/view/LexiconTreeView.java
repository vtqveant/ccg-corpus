package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.CustomTreeCellRenderer;
import ru.eventflow.ccg.annotation.ui.component.SecondaryTableCellRenderer;
import ru.eventflow.ccg.annotation.ui.model.LexiconTreeTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class LexiconTreeView extends JPanel implements SearchEnabled {

    private static final SecondaryTableCellRenderer secondaryCellRenderer = new SecondaryTableCellRenderer();
    private final JXTreeTable treeTable;

    public LexiconTreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 150));

        LexiconTreeTableModel treeTableModel = new LexiconTreeTableModel();
        treeTable = new JXTreeTable(treeTableModel);
        treeTable.setEditable(false);
        treeTable.setAutoCreateColumnsFromModel(false);
        treeTable.setAutoCreateRowSorter(false);
        treeTable.setDragEnabled(false);
        treeTable.setColumnMargin(0);
        treeTable.setFont(Defaults.SMALL_FONT);
        treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.getColumnModel().getColumn(1).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(2).setCellRenderer(secondaryCellRenderer);

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
        tableHeader.getColumnModel().getColumn(2).setMaxWidth(45);

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

}
