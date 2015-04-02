package ru.eventflow.annotation.view;

import javax.swing.*;
import java.awt.*;

public class DocumentsView extends JPanel {

    private JList docList;

    public DocumentsView() {
        setLayout(new BorderLayout());
        setSize(150, 150);

        docList = new JList();
        docList.setCellRenderer(new DocumentListCellRenderer());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.add(docList);
        add(scrollPane);
    }

    public ListModel getModel() {
        return docList.getModel();
    }

    public void setModel(ListModel model) {
        docList.setModel(model);
    }
}
