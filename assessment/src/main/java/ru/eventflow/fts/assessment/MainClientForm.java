package ru.eventflow.fts.assessment;

import ru.eventflow.fts.datasource.Document;
import ru.eventflow.fts.datasource.Query;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainClientForm extends JFrame {

    private JPanel rootPanel;
    private JPanel topPanel;
    private JButton loadButton;
    private JButton saveButton;
    private JButton relevantButton;
    private JButton nonRelevantButton;
    private JPanel documentsPanel;
    private JPanel assessmentPanel;
    private JTextPane queryDescriptionTextPane;
    private JTextPane documentTextPane;
    private JList docList;
    private JButton prevQueryButton;
    private JButton nextQueryButton;
    private JLabel queryLabel;

    public MainClientForm() {

        // TODO rewrite if needed
        JMenuBar menuBar = Stuff.getStuff();
        topPanel.add(menuBar);


        EntityManager em = Persistence.createEntityManagerFactory("fts-datasource").createEntityManager();
        List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        DefaultListModel<Document> listModel = new DefaultListModel<Document>();
        for (Document document : documents) {
            listModel.addElement(document);
        }
        docList.setModel(listModel);
        docList.setCellRenderer(new MyListCellRenderer());
        docList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                Document selectedValue = (Document) list.getSelectedValue();
                documentTextPane.setText(selectedValue.getText());
                documentTextPane.setCaretPosition(0);
            }
        });

        List<Query> queries = em.createQuery("SELECT x FROM Query x ORDER BY x.id ASC", Query.class).getResultList();
        if (queries.size() > 0) {
            queryLabel.setText(queries.get(0).getOrthography());
            queryDescriptionTextPane.setText(queries.get(0).getDescription());
        }
        if (queries.size() > 1) {
            nextQueryButton.setEnabled(true);
        }
    }


    public JPanel getRootPanel() {
        return rootPanel;
    }

    class MyListCellRenderer extends JLabel implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Document document = (Document) value;
            setText(String.format("%6s %20s", document.getId(), document.getUrl()));
            setOpaque(true);
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }
}
