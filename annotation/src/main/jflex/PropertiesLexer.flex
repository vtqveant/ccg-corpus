package ru.eventflow.ccg.annotation.lexer;

import java.io.*;

%%

%public
%class PropertiesLexer
%implements FlexLexer

%function next
%type Token

%unicode
%char

%{
    @Override
    public void reset(Reader reader) throws IOException {
        yyclose();
        yyreset(reader);
    }

    private Token token(int id) {
        String content = yytext();
        return new PropertiesToken(id, yychar, yychar + content.length());
    }
%}

CRLF=\R
WHITE_SPACE_CHAR=[\ \n\r\t\f]
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
KEY_SEPARATOR=[:=]
KEY_SEPARATOR_SPACE=\ \t
KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".
FIRST_VALUE_CHARACTER_BEFORE_SEP={VALUE_CHARACTER}
VALUE_CHARACTERS_BEFORE_SEP=([^:=\ \t\n\r\f\\] | "\\"{CRLF} | "\\".)({VALUE_CHARACTER}*)
VALUE_CHARACTERS_AFTER_SEP=([^\ \t\n\r\f\\] | "\\"{CRLF} | "\\".)({VALUE_CHARACTER}*)

%state IN_VALUE
%state IN_KEY_VALUE_SEPARATOR_HEAD
%state IN_KEY_VALUE_SEPARATOR_TAIL

%%

<YYINITIAL> {END_OF_LINE_COMMENT}        { yybegin(YYINITIAL); return token(PropertiesToken.END_OF_LINE_COMMENT); }

<YYINITIAL> {KEY_CHARACTER}+             { yybegin(IN_KEY_VALUE_SEPARATOR_HEAD); return token(PropertiesToken.KEY_CHARACTERS); }

<IN_KEY_VALUE_SEPARATOR_HEAD> {
    {KEY_SEPARATOR_SPACE}+               { yybegin(IN_KEY_VALUE_SEPARATOR_HEAD); return token(PropertiesToken.WHITE_SPACE); }
    {KEY_SEPARATOR}                      { yybegin(IN_KEY_VALUE_SEPARATOR_TAIL); return token(PropertiesToken.KEY_VALUE_SEPARATOR); }
    {VALUE_CHARACTERS_BEFORE_SEP}        { yybegin(YYINITIAL); return token(PropertiesToken.VALUE_CHARACTERS); }
    {CRLF}{WHITE_SPACE_CHAR}*            { yybegin(YYINITIAL); return token(PropertiesToken.WHITE_SPACE); }
}

<IN_KEY_VALUE_SEPARATOR_TAIL> {
    {KEY_SEPARATOR_SPACE}+               { yybegin(IN_KEY_VALUE_SEPARATOR_TAIL); return token(PropertiesToken.WHITE_SPACE); }
    {VALUE_CHARACTERS_AFTER_SEP}         { yybegin(YYINITIAL); return token(PropertiesToken.VALUE_CHARACTERS); }
    {CRLF}{WHITE_SPACE_CHAR}*            { yybegin(YYINITIAL); return token(PropertiesToken.WHITE_SPACE); }
}

<IN_VALUE> {VALUE_CHARACTER}+            { yybegin(YYINITIAL); return token(PropertiesToken.VALUE_CHARACTERS); }

<IN_VALUE> {CRLF}{WHITE_SPACE_CHAR}*     { yybegin(YYINITIAL); return token(PropertiesToken.WHITE_SPACE); }

{WHITE_SPACE_CHAR}+                      { return token(PropertiesToken.WHITE_SPACE); }

[^]                                      { return token(PropertiesToken.BAD_CHARACTER); }