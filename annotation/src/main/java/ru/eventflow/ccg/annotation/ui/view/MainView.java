package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JPanel {

    private final JPanel containerPanel = new JPanel(new BorderLayout());

    private final JButton navigationBtn = new JButton("Corpus");
    private final JButton dictionaryBtn = new JButton("Dictionary");
    private final JButton annotationBtn = new JButton("Annotation");
    private final JLabel statusLabel = new JLabel(" ");
    private final JSplitPane navigationSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

    public MainView() {
        setLayout(new BorderLayout());

        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, containerPanel, navigationSplitPane);
        splitPane.setDividerLocation(0.75);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        add(splitPane, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel(new BorderLayout());

        final JPanel horizontalButtonsPanel = new JPanel();
        horizontalButtonsPanel.setLayout(new BoxLayout(horizontalButtonsPanel, BoxLayout.LINE_AXIS));
        horizontalButtonsPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 5));
        horizontalButtonsPanel.add(navigationBtn);
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        horizontalButtonsPanel.add(dictionaryBtn);
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        horizontalButtonsPanel.add(annotationBtn);
        horizontalButtonsPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(horizontalButtonsPanel, BorderLayout.CENTER);

        statusLabel.setBorder(new EmptyBorder(2, 5, 2, 2));
        statusLabel.setFont(new Font("Sans", Font.PLAIN, 11));
        bottomPanel.add(statusLabel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public JPanel getContainerPanel() {
        return containerPanel;
    }

    public JButton getNavigationBtn() {
        return navigationBtn;
    }

    public JButton getDictionaryBtn() {
        return dictionaryBtn;
    }

    public JButton getAnnotationBtn() {
        return annotationBtn;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JSplitPane getNavigationSplitPane() {
        return navigationSplitPane;
    }

}
