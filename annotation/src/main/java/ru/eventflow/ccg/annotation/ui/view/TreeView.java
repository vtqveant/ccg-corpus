package ru.eventflow.ccg.annotation.ui.view;

import ru.eventflow.ccg.datasource.model.corpus.Document;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreeView extends JPanel {

    private final JTree tree;

    public TreeView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 300));

        DefaultMutableTreeNode top = new DefaultMutableTreeNode("OpenCorpora Collection");
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

    public TreeSelectionModel getSelectionModel() {
        return tree.getSelectionModel();
    }

    class DocumentTreeCellRenderer extends JLabel implements TreeCellRenderer {
        DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
                Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                if (userObject instanceof Document) {
                    Document doc = (Document) userObject;
                    setText(String.format("%-6s %-20s", doc.getId(), doc.getName()));
                    setFont(new Font("Courier New", Font.PLAIN, 11));
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
