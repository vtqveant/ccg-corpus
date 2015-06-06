package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.model.Context;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class ConcordanceView extends JPanel {

    private final JTextField filterField;
    private TableRowSorter<ContextTableModel> sorter;

    public ConcordanceView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        filterField = new JTextField();
        filterField.setFont(Defaults.SMALL_FONT);
        filterField.getDocument().addDocumentListener(new FilterDocumentListener());
        add(filterField, BorderLayout.PAGE_START);

        // setup the model
        List<Context> data = new ArrayList<>();
        data.add(new Context("итак, она", "звалась", "Татяьна", 234, false));
        data.add(new Context("Увы, она", "звалась", "Петровна", 354, true));
        data.add(new Context("Но она", "звалась", "Доминик", 1634, false));

        ContextTableModel model = new ContextTableModel(data);
        JTable table = new JTable(model);

        sorter = new TableRowSorter<ContextTableModel>(model);
        sorter.setSortable(0, false); // disable manual sorting for context, because it's behaviour is unclear
        sorter.setSortable(1, false);
        table.setRowSorter(sorter);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(selectionModel);

        // table adjustments
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setDragEnabled(false);
        table.setFont(Defaults.SMALL_FONT);
        table.setShowGrid(false);

        // setup column renderers
        DefaultTableCellRenderer leftTableCellRenderer = new DefaultTableCellRenderer();
        leftTableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);

        DefaultTableCellRenderer rightTableCellRenderer = new DefaultTableCellRenderer();
        rightTableCellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

        SecondaryTableCellRenderer secondaryTableCellRenderer = new SecondaryTableCellRenderer();

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setCellRenderer(rightTableCellRenderer);
        columnModel.getColumn(1).setCellRenderer(leftTableCellRenderer);
        columnModel.getColumn(2).setCellRenderer(secondaryTableCellRenderer);

        // setup header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);
        header.getColumnModel().getColumn(1).setHeaderRenderer(new LeftAlignHeaderRenderer(table));
        header.getColumnModel().getColumn(2).setMaxWidth(60);
        header.getColumnModel().getColumn(2).setMinWidth(60);
        header.getColumnModel().getColumn(3).setMaxWidth(60);
        header.getColumnModel().getColumn(3).setMinWidth(60);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private static class LeftAlignHeaderRenderer implements TableCellRenderer {

        DefaultTableCellRenderer renderer;

        public LeftAlignHeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
    }

    private class FilterDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            resetFilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            resetFilter();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            resetFilter();
        }

        private void resetFilter() {
            RowFilter<ContextTableModel, Object> filter;
            try {
                filter = RowFilter.regexFilter(filterField.getText());
            } catch (PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(filter);
        }
    }

    public class ContextTableModel extends AbstractTableModel {
        private final String[] columns = new String[]{"", "Context", "Sentence", "Approved"};
        private List<Context> data = new ArrayList<Context>();

        public ContextTableModel(List<Context> data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Context entry = data.get(rowIndex);
            if (columnIndex == 0) return entry.getLeft() + "  ";
            if (columnIndex == 1) return entry.getOccurence() + "  " + entry.getRight();
            if (columnIndex == 2) return entry.getSentenceId();
            if (columnIndex == 3) return entry.isApproved();
            return null;
        }

        public java.util.List<Context> getData() {
            return data;
        }
    }

}
