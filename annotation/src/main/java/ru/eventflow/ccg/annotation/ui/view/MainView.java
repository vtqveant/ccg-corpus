package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainView extends JPanel {

    public static final int DIVIDER_SIZE = 3;
    private final JPanel topPanel;
    private final JLabel statusLabel;
    private final JSplitPane mainSplitPane;

    private final JButton annotationBtn;

    private JPanel navigationPanel;
    private JPanel dictionaryPanel;
    private final JToggleButton navigationBtn;
    private final JToggleButton dictionaryBtn;

    public MainView() {
        setLayout(new BorderLayout());

        topPanel = new JPanel(new BorderLayout());
        navigationPanel = new JPanel(new BorderLayout());
        dictionaryPanel = new JPanel(new BorderLayout());

        // initial state is navigation panel set invisible
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, navigationPanel);
        mainSplitPane.setDividerLocation(0.75);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(DIVIDER_SIZE);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplitPane, BorderLayout.CENTER);

        navigationBtn = new JToggleButton("Corpus");
        navigationBtn.setFocusable(false);
        navigationBtn.setSelected(true);
        navigationBtn.addItemListener(new ToggleItemListener());

        dictionaryBtn = new JToggleButton("Dictionary");
        dictionaryBtn.setFocusable(false);
        dictionaryBtn.addItemListener(new ToggleItemListener());

        NoneSelectedButtonGroup group = new NoneSelectedButtonGroup();
        group.add(navigationBtn);
        group.add(dictionaryBtn);

        // TODO remove
        annotationBtn = new JButton("Annotation");

        final JPanel bottomPanel = new JPanel(new BorderLayout());

        final JPanel horizontalButtonsPanel = new JPanel();
        horizontalButtonsPanel.setLayout(new BoxLayout(horizontalButtonsPanel, BoxLayout.LINE_AXIS));
        horizontalButtonsPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        horizontalButtonsPanel.add(navigationBtn);
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        horizontalButtonsPanel.add(dictionaryBtn);
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        horizontalButtonsPanel.add(annotationBtn);
        horizontalButtonsPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(horizontalButtonsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(new EmptyBorder(2, 3, 2, 2));
        statusLabel.setFont(new Font("Sans", Font.PLAIN, 11));
        bottomPanel.add(statusLabel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JButton getAnnotationBtn() {
        return annotationBtn;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JPanel getNavigationPanel() {
        return navigationPanel;
    }

    public JPanel getDictionaryPanel() {
        return dictionaryPanel;
    }

    private class ToggleItemListener implements ItemListener {
        private int loc = 0;

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JToggleButton button = (JToggleButton) e.getSource();
                JPanel current = (JPanel) mainSplitPane.getBottomComponent();
                if (button == navigationBtn && current != navigationPanel) {
                    mainSplitPane.setBottomComponent(navigationPanel);
                }
                if (button == dictionaryBtn && current != dictionaryPanel) {
                    mainSplitPane.setBottomComponent(dictionaryPanel);
                }

                mainSplitPane.getBottomComponent().setVisible(true);
                mainSplitPane.setDividerLocation(loc);
                mainSplitPane.setDividerSize(DIVIDER_SIZE);
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                loc = mainSplitPane.getDividerLocation();
                mainSplitPane.setDividerSize(0);
                mainSplitPane.getBottomComponent().setVisible(false);
            }
        }
    }

    private class NoneSelectedButtonGroup extends ButtonGroup {
        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
                super.setSelected(model, selected);
            } else {
                clearSelection();
            }
        }
    }
}
