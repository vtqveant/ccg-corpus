package ru.eventflow.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JPanel {

    private final JSplitPane splitPane;
    private final JButton prevBtn = new JButton("Prev");
    private final JButton nextBtn = new JButton("Next");
    private final JButton relevantBtn = new JButton("Relevant");
    private final JButton nonrelevantBtn = new JButton("Nonrelevant");
    private final JLabel statusLabel = new JLabel();

    public MainView() {
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
        add(topPanel, BorderLayout.NORTH);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        add(splitPane, BorderLayout.CENTER);

        statusLabel.setBorder(new EmptyBorder(2, 5, 2, 2));
        statusLabel.setFont(new Font("Sans", Font.PLAIN, 11));
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
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

    public JLabel getStatusLabel() {
        return statusLabel;
    }


}
