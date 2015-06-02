package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.annotation.ui.component.SlidingPanel;

import javax.swing.*;
import java.awt.*;

public class NavigationView extends SlidingPanel {

    public static final String TITLE = "Navigation";
    private final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);

    public NavigationView() {
        setLayout(new BorderLayout());

        final JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(Defaults.SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);

        ImageIcon lockIcon = new ImageIcon(ClassLoader.getSystemResource("images/lock.gif"));
        JToggleButton ambiguousBtn = new JToggleButton(lockIcon);
        ambiguousBtn.setSelected(true);
        ambiguousBtn.setToolTipText("Show Ambiguous Sentences");
        ambiguousBtn.setFocusable(false);

        ImageIcon magnifyIcon = new ImageIcon(ClassLoader.getSystemResource("images/magnify.gif"));
        JToggleButton annotatedBtn = new JToggleButton(magnifyIcon);
        annotatedBtn.setSelected(true);
        annotatedBtn.setToolTipText("Show Annotated Sentences");
        annotatedBtn.setFocusable(false);

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.LINE_AXIS));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(0, 6, 3, 2));
        headingPanel.add(titleLabel);
        headingPanel.add(Box.createHorizontalGlue());
        headingPanel.add(annotatedBtn);
        headingPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        headingPanel.add(ambiguousBtn);
        add(headingPanel, BorderLayout.PAGE_START);

        add(splitPane, BorderLayout.CENTER);
    }

    public void setLeftComponent(Component component) {
        splitPane.setLeftComponent(component);
    }

    public void setRightComponent(Component component) {
        splitPane.setRightComponent(component);
    }

    @Override
    public ImageIcon getIcon() {
        return new ImageIcon(ClassLoader.getSystemResource("images/corpus.png"));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
