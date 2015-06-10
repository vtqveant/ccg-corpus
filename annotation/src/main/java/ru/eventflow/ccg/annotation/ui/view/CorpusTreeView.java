package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.CustomTreeCellRenderer;
import ru.eventflow.ccg.annotation.ui.component.SecondaryTableCellRenderer;
import ru.eventflow.ccg.annotation.ui.model.CorpusTreeTableModel;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CorpusTreeView extends JPanel {

    private static final SecondaryTableCellRenderer secondaryCellRenderer = new SecondaryTableCellRenderer();
    private final JXTreeTable treeTable;

    public CorpusTreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(300, 150));

        CorpusTreeTableModel model = new CorpusTreeTableModel();
        treeTable = new JXTreeTable(model);
        treeTable.setRootVisible(false);
        treeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        treeTable.setAutoCreateColumnsFromModel(false);
        treeTable.setAutoCreateRowSorter(false);
        treeTable.setDragEnabled(false);
        treeTable.setFont(Defaults.SMALL_FONT);
        treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.getColumnModel().getColumn(1).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(2).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(3).setCellRenderer(secondaryCellRenderer);

        DefaultTreeRenderer treeCellRenderer = new CustomTreeCellRenderer(new StringValue() {
            @Override
            public String getString(Object value) {
                if (value instanceof Text) {
                    return String.valueOf(((Text) value).getName());
                }
                return null;
            }
        });
        treeTable.setTreeCellRenderer(treeCellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);

        JTableHeader tableHeader = treeTable.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setDefaultRenderer(headerRenderer);
        tableHeader.getColumnModel().getColumn(1).setMinWidth(45);
        tableHeader.getColumnModel().getColumn(1).setMaxWidth(45);
        tableHeader.getColumnModel().getColumn(2).setMinWidth(45);
        tableHeader.getColumnModel().getColumn(2).setMaxWidth(45);
        tableHeader.getColumnModel().getColumn(3).setMinWidth(45);
        tableHeader.getColumnModel().getColumn(3).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(treeTable);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        add(scrollPane);
    }

    public JXTreeTable getTreeTable() {
        return treeTable;
    }

}

