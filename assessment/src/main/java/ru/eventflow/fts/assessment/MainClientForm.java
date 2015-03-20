package ru.eventflow.fts.assessment;

import ru.eventflow.fts.datasource.Document;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainClientForm extends JFrame {

    private JPanel rootPanel;
    private JPanel topPanel;
    private JButton loadButton;
    private JButton saveButton;
    private JButton relevantButton;
    private JButton nonRelevantButton;
    private JPanel documentsPanel;
    private JPanel assessmentPanel;
    private JTextPane relevanceTextPane;
    private JTextPane documentTextPane;
    private JList docList;

    public MainClientForm() {

        // where the GUI is created:
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;
        JCheckBoxMenuItem cbMenuItem;

        // create the menu bar.
        menuBar = new JMenuBar();

        // build the first menu.
        menu = new JMenu("A Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu);

        // a group of JMenuItems
        menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
        menu.add(menuItem);

        menuItem = new JMenuItem("Both text and icon", new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem);

        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
        menuItem.setMnemonic(KeyEvent.VK_D);
        menu.add(menuItem);

        // a group of radio button menu items
        menu.addSeparator();
        ButtonGroup group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Another one");
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        // a group of check box menu items
        menu.addSeparator();
        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
        cbMenuItem.setMnemonic(KeyEvent.VK_C);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Another one");
        cbMenuItem.setMnemonic(KeyEvent.VK_H);
        menu.add(cbMenuItem);

        // a submenu
        menu.addSeparator();
        submenu = new JMenu("A submenu");
        submenu.setMnemonic(KeyEvent.VK_S);

        menuItem = new JMenuItem("An item in the submenu");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        submenu.add(menuItem);

        menuItem = new JMenuItem("Another item");
        submenu.add(menuItem);
        menu.add(submenu);

        // build second menu in the menu bar.
        menu = new JMenu("Another Menu");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription("This menu does nothing");
        menuBar.add(menu);

        topPanel.add(menuBar);


        EntityManager em = Persistence.createEntityManagerFactory("fts-datasource").createEntityManager();
        List<Document> documents = em.createQuery("SELECT x FROM Document x ORDER BY x.id ASC", Document.class).getResultList();

        DefaultListModel<Document> listModel = new DefaultListModel<Document>();
        for (Document document : documents) {
            listModel.addElement(document);
        }
        docList.setModel(listModel);
        docList.setCellRenderer(new MyListCellRenderer());
        docList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                Document selectedValue = (Document) list.getSelectedValue();
                documentTextPane.setText(selectedValue.getText());
                documentTextPane.setCaretPosition(0);
            }
        });


//        relevanceTextPane.setText("sdfsdfsdf");
//        documentTextPane.setText("sdsdf");
    }


    public JPanel getRootPanel() {
        return rootPanel;
    }


    class MyListCellRenderer extends JLabel implements ListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Document document = (Document) value;
            String line = (document.getText().length() < 20) ?
                    document.getText() : document.getText().substring(0, 20) + "...";
            setText(String.format("%6s %20s", document.getId(), line));
            setOpaque(true);
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
            return this;
        }
    }
}
