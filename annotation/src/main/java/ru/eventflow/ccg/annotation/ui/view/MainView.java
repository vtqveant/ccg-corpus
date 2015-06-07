package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class MainView extends JPanel {

    public static final int DIVIDER_SIZE = 3;
    public static final double DIVIDER_LOCATION = 0.75d;

    private final JSplitPane mainSplitPane;

    private final JLabel statusLabel;
    private final JLabel caretPositionLabel;

    private final NoneSelectedButtonGroup group = new NoneSelectedButtonGroup();
    private final JPanel horizontalButtonsPanel = new JPanel();
    private final JPanel statusBarPanel;
    private final AboutDialog aboutDialog;

    public MainView() {
        setLayout(new BorderLayout());

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, null, null);
        mainSplitPane.setDividerLocation(DIVIDER_LOCATION);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setDividerSize(0);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setBorder(BorderFactory.createEmptyBorder());
        add(mainSplitPane, BorderLayout.CENTER);

        // to contain toggle buttons and a status bar at the bottom
        final JPanel bottomPanel = new JPanel(new BorderLayout());

        horizontalButtonsPanel.setLayout(new BoxLayout(horizontalButtonsPanel, BoxLayout.LINE_AXIS));
        horizontalButtonsPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 1, 2));
        horizontalButtonsPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(horizontalButtonsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(Defaults.SMALL_FONT);

        caretPositionLabel = new JLabel("n/a");
        caretPositionLabel.setFont(Defaults.SMALL_FONT);

        statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new BoxLayout(statusBarPanel, BoxLayout.LINE_AXIS));
        statusBarPanel.setBorder(BorderFactory.createEmptyBorder(1, 6, 2, 6));
        statusBarPanel.add(statusLabel);
        statusBarPanel.add(Box.createHorizontalGlue());
        statusBarPanel.add(caretPositionLabel);
        bottomPanel.add(statusBarPanel, BorderLayout.PAGE_END);

        add(bottomPanel, BorderLayout.PAGE_END);

        aboutDialog = new AboutDialog((JFrame) SwingUtilities.getWindowAncestor(this));
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getCaretPositionLabel() {
        return caretPositionLabel;
    }

    public AboutDialog getAboutDialog() {
        return aboutDialog;
    }

    public void setStatusBarVisible(boolean visible) {
        statusBarPanel.setVisible(visible);
    }

    public void setTopPanel(JPanel panel) {
        mainSplitPane.setTopComponent(panel);
    }

    public void addSliderPanel(SliderPanel panel) {
        JToggleButton button = new JToggleButton(panel.getTitle());
        button.setFocusable(false);
        button.setIcon(panel.getIcon());
        button.setMaximumSize(new Dimension(60, 22));
        button.setMinimumSize(new Dimension(60, 22));
        button.setFont(Defaults.SMALL_FONT);
        button.addItemListener(new ToggleItemListener(panel));
        group.add(button);

        // add button to the left
        horizontalButtonsPanel.add(Box.createRigidArea(new Dimension(2, 0)), 0);
        horizontalButtonsPanel.add(button, 0);

        if (mainSplitPane.getBottomComponent() == null) {
            mainSplitPane.setBottomComponent(panel);
        }
        panel.setVisible(false);
    }

    /**
     * holds the sliding panel visibility status
     */
    private class ToggleItemListener implements ItemListener {

        private int location = -1;
        private JPanel panel;

        public ToggleItemListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (panel != mainSplitPane.getBottomComponent()) {
                    mainSplitPane.setBottomComponent(panel);
                }
                panel.setVisible(true);
                if (location == -1) {
                    location = (int) ((double) (mainSplitPane.getHeight() -
                            mainSplitPane.getDividerSize()) * DIVIDER_LOCATION);
                }
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

    /**
     * to be able to deselect all buttons in the group
     */
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

    public class AboutDialog extends JDialog implements WindowFocusListener {

        private static final int WIDTH = 360;
        private static final int HEIGHT = 200;

        private static final String HTML = "<html>" +
                "<div height=\"110\">" +
                "Categorial Grammar Annotation for OpenCorpora<br/>" +
                "<div align=\"right\"><font size=\"-2\"><i>Inspired by Mathlingvo</i></font></div>" +
                "</div>" +
                "<div align=\"right\"><font size=\"-2\">by Kosta Sokolov (vtqveant@gmail.com)</font></div></html>";

        public AboutDialog(JFrame owner) {
            super(owner, ModalityType.MODELESS);

            setSize(WIDTH, HEIGHT);
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width / 2) - (WIDTH / 2);
            int y = (screenSize.height / 2) - (HEIGHT / 2);
            setLocation(x, y);

            setUndecorated(true);
            setAlwaysOnTop(true);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            addWindowFocusListener(this);

            JLabel label = new JLabel();
            label.setBackground(Color.LIGHT_GRAY);
            label.setText(HTML);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panel.add(label);
            setContentPane(panel);
        }

        @Override
        public void windowGainedFocus(WindowEvent e) {
            // nothing
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            AboutDialog.this.setVisible(false);
        }
    }

}
