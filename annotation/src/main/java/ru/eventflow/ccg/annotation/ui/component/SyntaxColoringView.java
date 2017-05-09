package ru.eventflow.ccg.annotation.ui.component;

import ru.eventflow.ccg.annotation.lexer.ColorScheme;
import ru.eventflow.ccg.annotation.lexer.FlexLexer;
import ru.eventflow.ccg.annotation.lexer.Token;

import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;

/**
 * Using PlainView here because we don't want line wrapping to occur
 */
public class SyntaxColoringView extends PlainView {

    private FlexLexer lexer;

    public SyntaxColoringView(Element element, FlexLexer lexer) {
        super(element);
        this.lexer = lexer;
        getDocument().putProperty(PlainDocument.tabSizeAttribute, 4); // set tabsize to 4 (instead of the default 8)
    }

    // TODO create a pool of reusable fonts, don't clone too often
    private static void set(Graphics graphics, ColorScheme.Style style) {
        AttributeSet attributes = ColorScheme.getAttributes(style);
        graphics.setColor(StyleConstants.getForeground(attributes));
        int mask = (StyleConstants.isBold(attributes) ? Font.BOLD : Font.PLAIN) |
                (StyleConstants.isItalic(attributes) ? Font.ITALIC : Font.PLAIN);
        Font f = graphics.getFont().deriveFont(mask);
        graphics.setFont(f);
    }

    @Override
    protected int drawUnselectedText(Graphics graphics, int x, int y, int p0, int p1) throws BadLocationException {
        Document doc = getDocument();
        String text = doc.getText(p0, p1 - p0);
        Segment segment = getLineBuffer();

        try {
            lexer.reset(new StringReader(text));
            Token t;
            int i = 0;
            while ((t = lexer.next()) != null) {
                int start = t.getStart();

                if (i < start) {
                    set(graphics, ColorScheme.Style.BODY);
                    doc.getText(p0 + i, start - i, segment);
                    x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
                }

                set(graphics, t.getStyle());
                i = t.getEnd();
                doc.getText(p0 + start, i - start, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, start);
            }

            // paint possible remaining text
            if (i < text.length()) {
                set(graphics, ColorScheme.Style.BODY);
                doc.getText(p0 + i, text.length() - i, segment);
                x = Utilities.drawTabbedText(segment, x, y, graphics, this, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return x;
    }

}