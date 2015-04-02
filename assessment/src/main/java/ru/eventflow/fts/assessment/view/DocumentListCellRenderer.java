package ru.eventflow.fts.assessment.view;

import ru.eventflow.fts.datasource.Document;

import javax.swing.*;
import java.awt.*;

class DocumentListCellRenderer extends JLabel implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Document document = (Document) value;
        setText(String.format("%6s %20s", document.getId(), document.getUrl()));
        setOpaque(true);
        setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
        return this;
    }
}