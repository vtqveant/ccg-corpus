package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class ProofAssistantView extends JPanel {

    private final JButton prevBtn = new JButton("Prev");
    private final JButton nextBtn = new JButton("Next");
    private final JButton relevantBtn = new JButton("Relevant");
    private final JButton nonrelevantBtn = new JButton("Nonrelevant");

    public ProofAssistantView() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        topPanel.add(prevBtn);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(nextBtn);
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        relevantBtn.setEnabled(false);
        topPanel.add(relevantBtn);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        nonrelevantBtn.setEnabled(false);
        topPanel.add(nonrelevantBtn);

        JScrollPane scrollPane = new JScrollPane(topPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.PAGE_START);
    }

    public JButton getPrevBtn() {
        return prevBtn;
    }

    public JButton getNextBtn() {
        return nextBtn;
    }

    public JButton getRelevantBtn() {
        return relevantBtn;
    }

    public JButton getNonrelevantBtn() {
        return nonrelevantBtn;
    }


}
