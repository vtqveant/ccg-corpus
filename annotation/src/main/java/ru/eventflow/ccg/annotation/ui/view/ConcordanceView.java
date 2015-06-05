package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class ConcordanceView extends JPanel {

    public ConcordanceView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        // setup the model
        TableModel model = new ContextTableModel();
        JTable table = new JTable(model);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(selectionModel);

        // table adjustments
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(Defaults.SMALL_FONT);
        table.setShowGrid(false);

        table.getColumnModel().getColumn(0).setCellRenderer(new SecondaryTableCellRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new SecondaryTableCellRenderer());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(45);
        table.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    // TODO implement context model
    public class ContextTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return 2;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) return "Hit";
            if (column == 1) return "Context";
            if (column == 2) return "Text Id";
            return "";
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) return "123";
            if (columnIndex == 1) return "На пятой пресс-конференции было аккредитовано";
            if (columnIndex == 2) return "23645";
            return null;
            // TODO implement
        }
    }

}
