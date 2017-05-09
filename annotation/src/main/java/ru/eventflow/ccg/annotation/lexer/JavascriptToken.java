package ru.eventflow.ccg.annotation.lexer;

public class JavascriptToken extends Token {

    public static final int KEYWORD = 0x100;
    public static final int TYPE = 0x200;
    public static final int OPERATOR = 0x300;
    public static final int NUMBER = 0x400;
    public static final int COMMENT = 0x500;
    public static final int IDENTIFIER = 0x600;
    public static final int STRING = 0x700;

    public JavascriptToken(int id, int start, int end) {
        super(id, start, end);
    }

    @Override
    public ColorScheme.Style getStyle() {
        if (check(0x1)) return ColorScheme.Style.BODY;
        if (check(0x2)) return ColorScheme.Style.TAG;
        if (check(0x3)) return ColorScheme.Style.OPERATOR;
        if (check(0x4)) return ColorScheme.Style.BODY;
        if (check(0x5)) return ColorScheme.Style.GRAYED_OUT;
        if (check(0x6)) return ColorScheme.Style.BODY;
        if (check(0x7)) return ColorScheme.Style.TEXT;
        return ColorScheme.Style.UNKNOWN;
    }
}
