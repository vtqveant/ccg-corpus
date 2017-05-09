package ru.eventflow.ccg.annotation.lexer;

public class SPARQLToken extends Token {

    public final static int KEYWORD = 0x100;
    public final static int OPERATOR = 0x200;
    public final static int STRING = 0x300;
    public final static int NUMBER = 0x400;
    public final static int COMMENT = 0x500;
    public final static int IRI_REF = 0x600;
    public final static int TYPE = 0x700;
    public final static int TYPE2 = 0x701;
    public final static int TYPE3 = 0x702;
    public final static int WHITESPACE = 0x800;
    public final static int BINDING = 0x900;

    public SPARQLToken(int id, int start, int end) {
        super(id, start, end);
    }

    @Override
    public ColorScheme.Style getStyle() {
        if (check(0x1)) return ColorScheme.Style.RESERVED_WORD;
        if (check(0x2)) return ColorScheme.Style.BODY;
        if (check(0x3)) return ColorScheme.Style.TEXT;
        if (check(0x4)) return ColorScheme.Style.VALUE;
        if (check(0x5)) return ColorScheme.Style.COMMENT;
        if (check(0x6)) return ColorScheme.Style.REFERENCE;
        if (check(0x7)) return ColorScheme.Style.BODY;
        if (check(0x8)) return ColorScheme.Style.WHITESPACE;
        if (check(0x9)) return ColorScheme.Style.IDENTIFIER;
        return ColorScheme.Style.UNKNOWN;
    }

}
