package ru.eventflow.ccg.annotation.lexer;

public class PropertiesToken extends Token {

    public final static int END_OF_LINE_COMMENT = 0x100;
    public final static int KEY_CHARACTERS = 0x200;
    public final static int KEY_VALUE_SEPARATOR = 0x300;
    public final static int VALUE_CHARACTERS = 0x400;
    public final static int WHITE_SPACE = 0x500;
    public final static int BAD_CHARACTER = 0x600;

    public PropertiesToken(int id, int start, int end) {
        super(id, start, end);
    }

    @Override
    public ColorScheme.Style getStyle() {
        if (check(0x1)) return ColorScheme.Style.GRAYED_OUT;
        if (check(0x2)) return ColorScheme.Style.TAG;
        if (check(0x3)) return ColorScheme.Style.BODY;
        if (check(0x4)) return ColorScheme.Style.TEXT;
        if (check(0x5)) return ColorScheme.Style.BODY;
        if (check(0x6)) return ColorScheme.Style.ERROR;
        return ColorScheme.Style.UNKNOWN;
    }
}
