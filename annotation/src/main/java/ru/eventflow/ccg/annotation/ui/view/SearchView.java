package ru.eventflow.ccg.annotation.ui.view;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import ru.eventflow.ccg.annotation.ui.Defaults;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO SwingX https://www.informit.com/guides/content.aspx?g=java&seqNum=528
public class SearchView extends JPanel {

    private static final SecondaryTableCellRenderer secondaryCellRenderer = new SecondaryTableCellRenderer();

    public SearchView() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(200, 150));

        MyTreeTableModel treeTableModel = new MyTreeTableModel();
        JXTreeTable treeTable = new JXTreeTable(treeTableModel);
        treeTable.setFont(Defaults.SMALL_FONT);
        treeTable.getColumnModel().getColumn(1).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(2).setCellRenderer(secondaryCellRenderer);
        treeTable.getColumnModel().getColumn(3).setCellRenderer(secondaryCellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        headerRenderer.setFont(Defaults.SMALL_FONT);
        headerRenderer.setBackground(new Color(245, 245, 245));
        headerRenderer.setForeground(Color.DARK_GRAY);
        treeTable.getTableHeader().setDefaultRenderer(headerRenderer);
        treeTable.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(80);
        treeTable.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(80);
        treeTable.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(45);

        treeTable.setTreeCellRenderer(new LexiconTreeCellRenderer());

        JScrollPane scrollPane = new JScrollPane(treeTable);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    private class LexiconTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            setFont(Defaults.SMALL_FONT);
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
    }

    class LexiconTreeNode {
        protected String form;
        protected String lemma;
        protected int count;
        protected java.util.List<String> grammemes = new ArrayList<>();
        protected java.util.List<LexiconTreeNode> children = new ArrayList<>();

        public LexiconTreeNode() {
        }

        public LexiconTreeNode(String form, String lemma, java.util.List<String> grammemes, int count) {
            this.count = count;
            this.form = form;
            this.lemma = lemma;
            this.grammemes.addAll(grammemes);
        }

        public String getForm() {
            return form;
        }

        public String getLemma() {
            return lemma;
        }

        public List<LexiconTreeNode> getChildren() {
            return children;
        }

        public int getCount() {
            return count;
        }

        public List<String> getGrammemes() {
            return grammemes;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (String grammeme : grammemes) {
                sb.append(grammeme);
                sb.append(", ");
            }
            if (grammemes.size() > 0) sb.delete(sb.length() - 2, sb.length() - 1);
            return sb.toString();
        }
    }

    private class SyntacticCategoryTreeNode extends LexiconTreeNode {

        private String category;

        public SyntacticCategoryTreeNode(String category, int count) {
            this.category = category;
            this.count = count;
        }

        @Override
        public String getForm() {
            return "";
        }

        @Override
        public String getLemma() {
            return "";
        }

        @Override
        public String toString() {
            return category;
        }
    }

    private class MyTreeTableModel extends AbstractTreeTableModel {
        private LexiconTreeNode root;

        public MyTreeTableModel() {
            root = new LexiconTreeNode(null, null, new ArrayList<String>(), -1);

            LexiconTreeNode form1 = new LexiconTreeNode("ноги", "нога", Arrays.asList("sg", "gen", "f"), 10);
            form1.getChildren().add(new SyntacticCategoryTreeNode("n", 5));
            form1.getChildren().add(new SyntacticCategoryTreeNode("n/n", 3));
            form1.getChildren().add(new SyntacticCategoryTreeNode("np", 2));
            root.getChildren().add(form1);

            LexiconTreeNode form2 = new LexiconTreeNode("ноги", "нога", Arrays.asList("pl", "nom", "f"), 15);
            form2.getChildren().add(new SyntacticCategoryTreeNode("n", 10));
            form2.getChildren().add(new SyntacticCategoryTreeNode("n/n", 5));
            root.getChildren().add(form2);
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Syntactic Category";
                case 1:
                    return "Form";
                case 2:
                    return "Lemma";
                case 3:
                    return "Count";
                default:
                    return "Unknown";
            }
        }

        @Override
        public Object getValueAt(Object node, int column) {
            LexiconTreeNode n = (LexiconTreeNode) node;
            if (column == 0) return n.toString();
            if (column == 1) return n.getForm();
            if (column == 2) return n.getLemma();
            if (column == 3) return n.getCount();
            return null;
        }

        @Override
        public Object getChild(Object node, int index) {
            LexiconTreeNode treenode = (LexiconTreeNode) node;
            return treenode.getChildren().get(index);
        }

        @Override
        public int getChildCount(Object parent) {
            LexiconTreeNode treenode = (LexiconTreeNode) parent;
            return treenode.getChildren().size();
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {
            LexiconTreeNode treenode = (LexiconTreeNode) parent;
            for (int i = 0; i > treenode.getChildren().size(); i++) {
                if (treenode.getChildren().get(i) == child) {
                    return i;
                }
            }

            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean isLeaf(Object node) {
            LexiconTreeNode treenode = (LexiconTreeNode) node;
            return treenode.getChildren().size() == 0;
        }

        @Override
        public Object getRoot() {
            return root;
        }
    }
}
