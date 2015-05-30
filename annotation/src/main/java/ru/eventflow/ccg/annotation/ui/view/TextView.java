package ru.eventflow.ccg.annotation.ui.view;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.AdvancedTableModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import ru.eventflow.ccg.datasource.model.corpus.Sentence;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Comparator;

/**
 * Glazed Lists, s. http://www.glazedlists.com/
 */
public class TextView extends JPanel {

    private EventList<Sentence> sentences;
    private JTable table;

    public TextView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        // setup the model
        sentences = new BasicEventList<Sentence>();

        // property names for getId() etc.
        String[] columnProperties = {"id", "source"};
        String[] columnLabels = {"Id", "Source"};
        TableFormat<Sentence> tableFormat = GlazedLists.<Sentence>tableFormat(columnProperties, columnLabels);

        SortedList<Sentence> sortedSentences = new SortedList<Sentence>(sentences, new SentenceComparator());
        AdvancedTableModel<Sentence> issuesTableModel = GlazedListsSwing.eventTableModelWithThreadProxyList(sortedSentences, tableFormat);
        table = new JTable(issuesTableModel);

        ListSelectionModel selectionModel = new DefaultEventSelectionModel<Sentence>(sentences);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(selectionModel);

        // table adjustments
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        table.setShowGrid(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        centerRenderer.setForeground(Color.GRAY);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        headerRenderer.setBackground(new Color(230, 230, 230));
        headerRenderer.setForeground(Color.DARK_GRAY);
        table.getTableHeader().setDefaultRenderer(headerRenderer);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(45);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }

    public EventList<Sentence> getSentences() {
        return sentences;
    }

    private class SentenceComparator implements Comparator<Sentence> {
        @Override
        public int compare(Sentence o1, Sentence o2) {
            return o1.getId() - o2.getId();
        }
    }
}
