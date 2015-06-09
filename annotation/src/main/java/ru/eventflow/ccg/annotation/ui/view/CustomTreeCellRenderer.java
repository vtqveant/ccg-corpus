package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import java.awt.*;

/**
 * This is an extension of DefaultTreeRenderer from SwingX library, to be used with JXTreeTable
 */
public class CustomTreeCellRenderer extends DefaultTreeRenderer {
    public CustomTreeCellRenderer() {
    }

    public CustomTreeCellRenderer(StringValue sv) {
        super(sv);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        component.setFont(Defaults.SMALL_FONT);
        return component;
    }
}