package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.lexer.XMLLexer;
import ru.eventflow.ccg.annotation.ui.model.ContentType;

import javax.swing.text.Element;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;
import java.io.StringReader;

public class XMLEditorKit extends StyledEditorKit {

    private ViewFactory viewFactory;

    public XMLEditorKit() {
        viewFactory = (Element element) -> new SyntaxColoringView(element, new XMLLexer(new StringReader("")));
    }

    @Override
    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    @Override
    public String getContentType() {
        return ContentType.TEXT_XML;
    }

}