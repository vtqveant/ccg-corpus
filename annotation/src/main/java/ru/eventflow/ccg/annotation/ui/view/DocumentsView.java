package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.model.Document;

import javax.swing.*;
import java.awt.*;

public class DocumentsView extends JPanel {

    private DefaultListModel<Document> model = new DefaultListModel<Document>();
    private final JList list;

    public DocumentsView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 300));

        list = new JList();
        list.setModel(model);
        list.setCellRenderer(new DocumentListCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane);
    }

    public DefaultListModel<Document> getModel() {
        return model;
    }

    public ListSelectionModel getSelectionModel() {
        return list.getSelectionModel();
    }

    class DocumentListCellRenderer extends JLabel implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Document doc = (Document) value;
            setText(String.format("%6s %20s", doc.getId(), doc.getUrl()));
            setFont(new Font("Courier New", Font.PLAIN, 11));
            setOpaque(true);
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }
}
