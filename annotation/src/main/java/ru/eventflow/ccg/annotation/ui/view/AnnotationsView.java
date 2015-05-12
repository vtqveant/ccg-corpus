package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

public class AnnotationsView extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public static final ImageIcon ICON = new ImageIcon(ClassLoader.getSystemResource("images/close.png"));
//    public static final ImageIcon ROLLOVER_ICON = new ImageIcon(ClassLoader.getSystemResource("images/10.png"));

    public AnnotationsView() {
        setLayout(new BorderLayout());
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public void addTab(String title, Component component) {
        int idx = tabbedPane.getTabCount();
        tabbedPane.addTab(title, component);
        tabbedPane.setTabComponentAt(idx, new TabComponent());
    }

    private class TabComponent extends JPanel {

        public TabComponent() {
            setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 0));

            // read titles from JTabbedPane
            JLabel label = new JLabel() {
                public String getText() {
                    int i = tabbedPane.indexOfTabComponent(TabComponent.this);
                    if (i != -1) {
                        return tabbedPane.getTitleAt(i);
                    }
                    return null;
                }
            };
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            add(label);

            JButton button = new JButton();
            button.setIcon(ICON);
//            button.setRolloverIcon(ROLLOVER_ICON);
            button.setUI(new BasicButtonUI());
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setRolloverEnabled(true);
            button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i = tabbedPane.indexOfTabComponent(TabComponent.this);
                    if (i != -1) {
                        tabbedPane.remove(i);
                    }
                }
            });
            add(button);
        }
    }

}
