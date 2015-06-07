package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.model.Context;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.regex.PatternSyntaxException;

public class ConcordanceView extends JPanel {

    private static final String[] columns = new String[]{"", "Context", "Sentence", "Approved"};

    private final JTextField filterField;
    private final JTable table;
    private TableRowSorter<ContextTableModel> sorter;


    public ConcordanceView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        filterField = new JTextField();
        filterField.setFont(Defaults.SMALL_FONT);
        filterField.getDocument().addDocumentListener(new FilterDocumentListener());
        add(filterField, BorderLayout.PAGE_START);

        // setup the model
        ContextTableModel model = new ContextTableModel(columns, 0);
        table = new JTable(model);

        sorter = new TableRowSorter<ContextTableModel>(model);
        sorter.setSortable(0, false); // disable manual sorting for context, because it's behaviour is unclear
        sorter.setSortable(1, false);
        table.setRowSorter(sorter);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(selectionModel);

        // table adjustments
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateColumnsFromModel(false);
        table.setAutoCreateRowSorter(false);
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
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * complete rebuild of a model with many consequences, which have to be dealt with
     */
    public void setData(java.util.List<Context> contexts) {
        filterField.setText("");
        table.clearSelection();
        ContextTableModel model = (ContextTableModel) table.getModel();
        int size = contexts.size();
        Object[][] rows = new Object[size][columns.length];
        for (int i = 0; i < size; i++) {
            Context context = contexts.get(i);
            rows[i][0] = context.getLeft() + "  ";
            rows[i][1] = context.getOccurence() + "  " + context.getRight();
            rows[i][2] = context.getSentenceId();
            rows[i][3] = context.isApproved();
        }
        model.setDataVector(rows, columns);
        model.setRowCount(size);
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
            RowFilter<DefaultTableModel, Object> filter;
            try {
                filter = RowFilter.regexFilter(filterField.getText(), 0, 1, 2);
            } catch (PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(filter);
        }
    }

    private class ContextTableModel extends DefaultTableModel {

        public ContextTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return Integer.class;
                case 3:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

}
