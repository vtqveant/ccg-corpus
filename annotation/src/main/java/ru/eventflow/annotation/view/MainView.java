package ru.eventflow.annotation.view;

import ru.eventflow.annotation.Stuff;
import ru.eventflow.annotation.presenter.MainPresenter;

import javax.swing.*;
import java.awt.*;

public class MainView extends JPanel {

    private JPanel topPanel;
    private JPanel documentsPanel;
    private JPanel assessmentPanel;

    public MainView() {
        setLayout(new BorderLayout());

        // TODO rewrite if you need a menu
        topPanel = new JPanel();
        JMenuBar menuBar = Stuff.getStuff();
        topPanel.add(menuBar);
        add(topPanel);

        assessmentPanel = new JPanel();
        add(assessmentPanel);

        documentsPanel = new JPanel();
        add(documentsPanel);
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JPanel getDocumentsPanel() {
        return documentsPanel;
    }

    public JPanel getAssessmentPanel() {
        return assessmentPanel;
    }
}
