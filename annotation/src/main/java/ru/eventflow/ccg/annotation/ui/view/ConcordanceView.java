package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.LazyJTableDataSource;
import ru.eventflow.ccg.annotation.ui.component.LazyJTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ConcordanceView extends JPanel implements SearchEnabled {

    public static final String[] COLUMN_NAMES = new String[]{"", "Context", "Sent.", "Approved"};
    public static final Class[] COLUMN_CLASSES = new Class[]{String.class, String.class, Integer.class, Boolean.class};

    private final JTable table;
    private final LazyJTableModel emptyModel;

    public ConcordanceView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        //  add(searchPanel, BorderLayout.PAGE_START);

        // setup the model
        emptyModel = new LazyJTableModel(COLUMN_NAMES, COLUMN_CLASSES, 0);
        table = new JTable(emptyModel);

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

    public JTable getTable() {
        return table;
    }

    @Override
    public void addSearchPanel(SearchView searchPanel) {
        add(searchPanel, BorderLayout.PAGE_START);
    }

    /**
     * complete rebuild of a model with many consequences, which have to be dealt with
     */
    public void setDataSource(LazyJTableDataSource dataSource) {
        table.clearSelection();
        LazyJTableModel model;
        if (dataSource != null) {
            model = new LazyJTableModel(COLUMN_NAMES, COLUMN_CLASSES, dataSource.getTotalRowCount());
            model.setTableDataSource(dataSource);
        } else {
            model = emptyModel;
        }
        table.setModel(model);
        table.repaint();
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
}
