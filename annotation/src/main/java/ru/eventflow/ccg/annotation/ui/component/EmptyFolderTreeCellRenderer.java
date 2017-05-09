package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.project.FolderElement;
import ru.eventflow.ccg.annotation.project.ProjectElement;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * A renderer which displays empty folders as non-expandable folders (and not as leafs)
 */
public class EmptyFolderTreeCellRenderer extends DefaultTreeCellRenderer {

    JLabel renderer = new JLabel();
    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Component returnValue = null;
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof ProjectElement) {
                if (userObject instanceof FolderElement) {
                    if (expanded) {
                        renderer.setIcon(getDefaultOpenIcon());
                    } else {
                        renderer.setIcon(getDefaultClosedIcon());
                    }
                } else {
                    renderer.setIcon(getLeafIcon());
                }
                if (selected) {
                    renderer.setBackground(getBackgroundSelectionColor());
                } else {
                    renderer.setBackground(getBackgroundNonSelectionColor());
                }
                renderer.setOpaque(true);
                renderer.setText(((ProjectElement) userObject).getName());
                returnValue = renderer;
            }
        }
        if (returnValue == null) {
            returnValue = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
        return returnValue;
    }

}
