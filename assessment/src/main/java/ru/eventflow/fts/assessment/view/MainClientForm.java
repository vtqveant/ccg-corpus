package ru.eventflow.fts.assessment.view;

import ru.eventflow.fts.assessment.Stuff;
import ru.eventflow.fts.assessment.model.DocumentListModel;
import ru.eventflow.fts.datasource.Document;
import ru.eventflow.fts.datasource.Query;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
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
    private JPanel queryPanel;
    private JScrollPane documentScrollPane;


    public MainClientForm() {

        // TODO rewrite if needed
        JMenuBar menuBar = Stuff.getStuff();
        topPanel.add(menuBar);


        EntityManager em = Persistence.createEntityManagerFactory("fts-datasource").createEntityManager();
        List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        DocumentListModel documentListModel = new DocumentListModel();
        for (Document document : documents) {
            documentListModel.addElement(document);
        }

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


}
