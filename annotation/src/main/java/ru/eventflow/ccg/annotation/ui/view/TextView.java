package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TextView extends JPanel {

    private JTable table;

    public TextView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        // setup the model
        TableModel model = new SentenceTableModel();
        table = new JTable(model);

        ListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(selectionModel);

        // table adjustments
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(Defaults.SMALL_FONT);
        table.setShowGrid(false);

        table.getColumnModel().getColumn(0).setCellRenderer(new SecondaryTableCellRenderer());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }

    public class SentenceTableModel extends AbstractTableModel {

        private List<Sentence> sentences = new ArrayList<>();

        @Override
        public int getRowCount() {
            return sentences.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) return "Id";
            if (column == 1) return "Source";
            return "";
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return sentences.get(rowIndex).getId();
            } else {
                return sentences.get(rowIndex).getSource();
            }
        }

        public List<Sentence> getSentences() {
            return sentences;
        }
    }
}
