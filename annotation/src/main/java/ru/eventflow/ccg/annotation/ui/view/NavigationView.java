package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import java.awt.*;

public class NavigationView extends JPanel {

    public static final Font HEADING_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
    private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

    private final JLabel infoLabel = new JLabel(" ");

    public NavigationView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel("Navigation");
        titleLabel.setFont(HEADING_FONT);
        titleLabel.setForeground(Color.GRAY);

        infoLabel.setFont(HEADING_FONT);
        infoLabel.setForeground(Color.DARK_GRAY);

        JToggleButton lockBtn = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/lock.gif")));
        lockBtn.setFocusable(false);

        JToggleButton hideBtn = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/magnify.gif")));
        hideBtn.setFocusable(false);

        final JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(infoLabel);
        headingPanel.add(Box.createRigidArea(new Dimension(6, 0)));
        headingPanel.add(hideBtn);
        headingPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        headingPanel.add(lockBtn);
        add(headingPanel, BorderLayout.PAGE_START);

        add(splitPane, BorderLayout.CENTER);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
    }

    public void setInfo(String text) {
        infoLabel.setText(text);
    }

}
