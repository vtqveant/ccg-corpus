package ru.eventflow.ccg.annotation.ui.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * In this class I have a workaround for setting a divider location
 * (tricky because we create and remove an annotation views dynamically)
 */
public class AnnotationView extends JPanel {

    private final JTextArea goalsTextArea;
    private final JTextPane textPane;
    private final JSplitPane splitPane;
    private final AdjustableScrollPane glossesScrollPane;
    AbstractDocument doc;
    private boolean painted;

    public AnnotationView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());

        // editor
        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPane.setMargin(new Insets(5, 5, 5, 5));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
            // doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }

        JScrollPane sp1 = new JScrollPane(textPane);
        sp1.setSize(new Dimension(600, -1));
        sp1.setBorder(BorderFactory.createEmptyBorder());

        // goals
        goalsTextArea = new JTextArea(1, 30);
        goalsTextArea.setEditable(false);
        goalsTextArea.setFocusable(false);
        goalsTextArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        goalsTextArea.setBackground(new Color(243, 243, 243));
        goalsTextArea.setText("1 goal");

        JScrollPane sp2 = new JScrollPane(goalsTextArea);
        sp2.setBorder(BorderFactory.createEmptyBorder());
        sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        splitPane.setOneTouchExpandable(false);
        splitPane.setResizeWeight(0.5);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setLeftComponent(sp1);
        splitPane.setRightComponent(sp2);
        add(splitPane, BorderLayout.CENTER);

        // glosses at the bottom
        glossesScrollPane = new AdjustableScrollPane();
        glossesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        glossesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(glossesScrollPane, BorderLayout.PAGE_END);
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void setGlosses(Component glosses) {
        glossesScrollPane.setViewportView(glosses);
    }

    /**
     * A nasty hack to correctly display splitPane's divider location.
     * A normal approach doesn't work, because the splitpane is not visible when it's created.
     * <p>
     * s. http://stackoverflow.com/questions/2311449/jsplitpane-splitting-50-precisely
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!painted) {
            painted = true;
            splitPane.setDividerLocation(0.75);
        }
    }


}
