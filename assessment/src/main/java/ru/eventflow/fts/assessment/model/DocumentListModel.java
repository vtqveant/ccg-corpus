package ru.eventflow.fts.assessment.model;

import ru.eventflow.fts.datasource.Document;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DocumentListModel extends DefaultListModel<Document> implements PropertyChangeListener {

    public DocumentListModel() {
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        System.out.println(propertyName);
    }
}
