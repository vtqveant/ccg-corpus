package ru.eventflow.ccg.annotation.ui.component;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;

import static ru.eventflow.ccg.annotation.ui.model.ContentType.*;

/**
 * A JTextPane with syntax highlighting
 * <p>
 * s. https://github.com/kdekooter/xml-text-editor
 */
public class SyntaxColoringTextPane extends JTextPane {

    public SyntaxColoringTextPane() {
        setEditorKitForContentType(TEXT_PLAIN, new StyledEditorKit());
        setEditorKitForContentType(TEXT_SPARQL, new SPARQLEditorKit());
        setEditorKitForContentType(TEXT_PROPERTIES, new PropertiesEditorKit());
        setEditorKitForContentType(TEXT_XML, new XMLEditorKit());
        setEditorKitForContentType(APPLICATION_JAVASCRIPT, new JavascriptEditorKit());
        setEditorKitForContentType(APPLICATION_JSON, new JavascriptEditorKit());
        setEditorKitForContentType(APPLICATION_JSON_LD, new JavascriptEditorKit());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        super.paintComponent(g);
    }
}
