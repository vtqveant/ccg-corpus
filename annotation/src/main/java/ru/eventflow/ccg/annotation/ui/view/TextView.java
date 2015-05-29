package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class TextView extends JPanel {

    private JTable table = new JTable();

    public TextView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        table.getTableHeader().setForeground(Color.GRAY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getTable() {
        return table;
    }
}
