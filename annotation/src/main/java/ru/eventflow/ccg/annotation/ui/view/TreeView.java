package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.annotation.ui.Defaults;
import ru.eventflow.ccg.datasource.model.corpus.Text;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class TreeView extends JPanel {

    private final JTree tree;

    public TreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("OpenCorpora");
        tree = new JTree(top);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new DocumentTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane);
    }

    public JTree getTree() {
        return tree;
    }

    class DocumentTreeCellRenderer extends JLabel implements TreeCellRenderer {
        DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
                Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                if (userObject instanceof Text) {
                    Text text = (Text) userObject;
                    setText(String.format("%-4s %-20s", text.getId(), text.getName()));
                    setFont(Defaults.SMALL_FONT);
                    setOpaque(true);
                    setBackground(selected ? Color.LIGHT_GRAY : Color.WHITE);
                    return this;
                } else {
                    return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                }
            }
            return null;
        }
    }


}
