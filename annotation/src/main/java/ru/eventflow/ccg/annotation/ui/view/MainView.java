package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainView extends JPanel {

    public static final int DIVIDER_SIZE = 3;
    public static final double DIVIDER_LOCATION = 0.7d;
    private final JPanel topPanel;
    private final JLabel statusLabel;
    private final JSplitPane mainSplitPane;
    private final JToggleButton navigationBtn;
    private final JToggleButton dictionaryBtn;
    private JPanel navigationPanel;
    private JPanel dictionaryPanel;

    public MainView() {
        setLayout(new BorderLayout());

        topPanel = new JPanel(new BorderLayout());
        navigationPanel = new JPanel(new BorderLayout());
        dictionaryPanel = new JPanel(new BorderLayout());

        // initial state is navigation panel set invisible
        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, navigationPanel);
        mainSplitPane.setDividerLocation(DIVIDER_LOCATION);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(DIVIDER_SIZE);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplitPane, BorderLayout.CENTER);

        // all bottom sliding panes start in this position (pane's size has an effect, too)
        int location = mainSplitPane.getDividerLocation();

        navigationBtn = new JToggleButton("Navigation");
        navigationBtn.setFocusable(false);
        navigationBtn.setSelected(true);
        ImageIcon folderIcon = new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));
        navigationBtn.setIcon(folderIcon);
        navigationBtn.setMaximumSize(new Dimension(60, 22));
        navigationBtn.setMinimumSize(new Dimension(60, 22));
        navigationBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        navigationBtn.addItemListener(new ToggleItemListener(location));

        dictionaryBtn = new JToggleButton("Dictionary");
        dictionaryBtn.setFocusable(false);
        ImageIcon magnifyIcon = new ImageIcon(ClassLoader.getSystemResource("images/lookup.png"));
        dictionaryBtn.setIcon(magnifyIcon);
        dictionaryBtn.setMaximumSize(new Dimension(60, 22));
        dictionaryBtn.setMinimumSize(new Dimension(60, 22));
        dictionaryBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        dictionaryBtn.addItemListener(new ToggleItemListener(location));

        NoneSelectedButtonGroup group = new NoneSelectedButtonGroup();
        group.add(navigationBtn);
        group.add(dictionaryBtn);

        final JPanel bottomPanel = new JPanel(new BorderLayout());

        final JPanel horizontalButtonsPanel = new JPanel();
        horizontalButtonsPanel.setLayout(new BoxLayout(horizontalButtonsPanel, BoxLayout.LINE_AXIS));
        horizontalButtonsPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 1, 2));
        horizontalButtonsPanel.add(navigationBtn);
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        horizontalButtonsPanel.add(dictionaryBtn);
        horizontalButtonsPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(horizontalButtonsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setBorder(new EmptyBorder(1, 6, 2, 2));
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        bottomPanel.add(statusLabel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public JPanel getTopPanel() {
        return topPanel;
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
        private int location;

        public ToggleItemListener(int location) {
            this.location = location;
        }

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
                mainSplitPane.setDividerLocation(location);
                mainSplitPane.setDividerSize(DIVIDER_SIZE);
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                location = mainSplitPane.getDividerLocation();
                mainSplitPane.setDividerSize(0);
                mainSplitPane.getBottomComponent().setVisible(false);
            }
        }
    }

    private class NoneSelectedButtonGroup extends ButtonGroup {
        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
                super.setSelected(model, true);
            } else {
                clearSelection();
            }
        }
    }
}
