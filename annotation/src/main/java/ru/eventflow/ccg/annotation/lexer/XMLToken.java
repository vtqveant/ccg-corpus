package ru.eventflow.ccg.annotation.lexer;

public class XMLToken extends Token {

    public static final int KEYWORD = 0x100;
    public static final int TYPE = 0x200;
    public static final int TYPE2 = 0x201;
    public static final int COMMENT = 0x300;
    public static final int COMMENT2 = 0x301;
    public static final int IDENTIFIER = 0x400;
    public static final int STRING = 0x500;

    public XMLToken(int id, int start, int end) {
        super(id, start, end);
    }

    @Override
    public ColorScheme.Style getStyle() {
        if (check(0x1)) return ColorScheme.Style.NAME;
        if (check(0x2)) return ColorScheme.Style.TAG;
        if (check(0x3)) return ColorScheme.Style.GRAYED_OUT;
        if (check(0x4)) return ColorScheme.Style.LITERAL;
        if (check(0x5)) return ColorScheme.Style.TEXT;
        return ColorScheme.Style.UNKNOWN;
    }
}
