package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MainView extends JPanel {

    public static final int DIVIDER_SIZE = 3;
    public static final double DIVIDER_LOCATION = 0.75d;

    private final JSplitPane mainSplitPane;

    private final JLabel statusLabel;
    private final JLabel caretPositionLabel;

    private final JPanel topPanel = new JPanel(new BorderLayout());
    private final JPanel navigationPanel = new JPanel(new BorderLayout());
    private final JPanel dictionaryPanel = new JPanel(new BorderLayout());

    public MainView() {
        setLayout(new BorderLayout());

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, navigationPanel);
        mainSplitPane.setDividerLocation(DIVIDER_LOCATION);
        mainSplitPane.setResizeWeight(0.8);
        mainSplitPane.setDividerSize(DIVIDER_SIZE);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplitPane, BorderLayout.CENTER);

        ImageIcon folderIcon = new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));
        JToggleButton navigationBtn = createToggleButton("Navigation", folderIcon, navigationPanel);
        navigationBtn.setSelected(true);

        ImageIcon magnifyIcon = new ImageIcon(ClassLoader.getSystemResource("images/lookup.png"));
        JToggleButton dictionaryBtn = createToggleButton("Dictionary", magnifyIcon, dictionaryPanel);

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

        // status line and info
        statusLabel = new JLabel(" ");
        statusLabel.setFont(Defaults.SMALL_FONT);

        caretPositionLabel = new JLabel("n/a");
        caretPositionLabel.setFont(Defaults.SMALL_FONT);

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(1, 6, 2, 6));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(caretPositionLabel);
        bottomPanel.add(infoPanel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getCaretPositionLabel() {
        return caretPositionLabel;
    }

    public void setNavigationPanel(JPanel navigationPanel) {
        this.navigationPanel.add(navigationPanel);
    }

    public void setDictionaryPanel(JPanel dictionaryPanel) {
        this.dictionaryPanel.add(dictionaryPanel);
    }

    private JToggleButton createToggleButton(String text, ImageIcon icon, JPanel panel) {
        JToggleButton button = new JToggleButton(text);
        button.setFocusable(false);
        button.setIcon(icon);
        button.setMaximumSize(new Dimension(60, 22));
        button.setMinimumSize(new Dimension(60, 22));
        button.setFont(Defaults.SMALL_FONT);
        button.addItemListener(new ToggleItemListener(panel));
        return button;
    }

    /**
     * holds the SliderPane visibility status
     */
    private class ToggleItemListener implements ItemListener {

        private int location;

        /**
         * panel to track
         */
        private JPanel panel;

        public ToggleItemListener(JPanel panel) {
            this.location = (int) ((double) (mainSplitPane.getHeight() -
                    mainSplitPane.getDividerSize()) * DIVIDER_LOCATION);
            this.panel = panel;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (panel != mainSplitPane.getBottomComponent()) {
                    mainSplitPane.setBottomComponent(panel);
                }
                panel.setVisible(true);
                mainSplitPane.setDividerLocation(location);
                mainSplitPane.setDividerSize(DIVIDER_SIZE);
            }

            if (e.getStateChange() == ItemEvent.DESELECTED) {
                location = mainSplitPane.getDividerLocation();
                mainSplitPane.setDividerSize(0);
                panel.setVisible(false);
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
