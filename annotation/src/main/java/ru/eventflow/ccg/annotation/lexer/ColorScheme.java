package ru.eventflow.ccg.annotation.lexer;

import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class ColorScheme {

    private static final Color red = new Color(0x800000);
    private static final Color blue = new Color(0x000080);
    private static final Color green = new Color(0x008000);
    private static final Color purple = new Color(0x660e7a);
    private static final Color gray = new Color(0x808080);

    private static final MutableAttributeSet defaultAttributes = getDefaultAttributes();

    private static Map<Style, AttributeSet> dispatcher = new EnumMap<>(Style.class);

    static {
        dispatcher.put(Style.BODY, defaultAttributes);
        dispatcher.put(Style.TAG, derive(blue, true, false));
        dispatcher.put(Style.ENDTAG, derive(blue, false, false));
        dispatcher.put(Style.REFERENCE, derive(Color.BLACK, false, true));
        dispatcher.put(Style.NAME, derive(Color.BLACK, true, false));
        dispatcher.put(Style.VALUE, derive(Color.BLACK, false, false));
        dispatcher.put(Style.TEXT, derive(green, true, false));
        dispatcher.put(Style.RESERVED_WORD, derive(blue, true, false));
        dispatcher.put(Style.IDENTIFIER, derive(Color.BLACK, true, false));
        dispatcher.put(Style.LITERAL, derive(red, false, false));
        dispatcher.put(Style.SEPARATOR, derive(blue, false, false));
        dispatcher.put(Style.OPERATOR, derive(Color.BLACK, true, false));
        dispatcher.put(Style.COMMENT, derive(gray, false, true));
        dispatcher.put(Style.PREPROCESSOR, derive(purple, false, false));
        dispatcher.put(Style.WHITESPACE, defaultAttributes);
        dispatcher.put(Style.ERROR, derive(Color.RED, false, false));
        dispatcher.put(Style.UNKNOWN, derive(Color.ORANGE, false, false));
        dispatcher.put(Style.GRAYED_OUT, derive(Color.GRAY, false, false));
    }

    private static MutableAttributeSet getDefaultAttributes() {
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBackground(attributes, Color.WHITE);
        StyleConstants.setForeground(attributes, Color.BLACK);
        StyleConstants.setFontFamily(attributes, Font.MONOSPACED);
        StyleConstants.setFontSize(attributes, 12);
        StyleConstants.setBold(attributes, false);
        StyleConstants.setItalic(attributes, false);
        return attributes;
    }

    private static AttributeSet derive(Color color, boolean bold, boolean italic) {
        MutableAttributeSet attributes = (MutableAttributeSet) defaultAttributes.copyAttributes();
        StyleConstants.setForeground(attributes, color);
        StyleConstants.setBold(attributes, bold);
        StyleConstants.setItalic(attributes, italic);
        return attributes;
    }

    public static AttributeSet getAttributes(Style style) {
        return dispatcher.get(style);
    }

    /**
     * Different styles of tokens to be used in syntax highlighting
     */
    public enum Style {
        BODY, TAG, ENDTAG, REFERENCE, NAME, VALUE, TEXT, RESERVED_WORD, IDENTIFIER, LITERAL,
        SEPARATOR, OPERATOR, COMMENT, PREPROCESSOR, WHITESPACE, ERROR, UNKNOWN, GRAYED_OUT
    }
}
