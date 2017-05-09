/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.eventflow.ccg.annotation.lexer;

import java.io.*;

%%

%public
%class JavascriptLexer
%implements FlexLexer
%final

%function next
%type Token

%unicode
%char

%{
    private int tokenStart;
    private int tokenLength;

    @Override
    public void reset(Reader reader) throws IOException {
        yyclose();
        yyreset(reader);
    }

    private Token token(int id) {
        String content = yytext();
        return new JavascriptToken(id, yychar, yychar + content.length());
    }

    private Token token(int id, int start, int length) {
        return new JavascriptToken(id, start, start + length);
    }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]

/* floating point literals */
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]

%state STRING

%%

<YYINITIAL> {

  /* keywords */
  "break"                        |
  "case"                         |
  "catch"                        |
  "continue"                     |
  "do"                           |
  "else"                         |
  "finally"                      |
  "for"                          |
  "default"                      |
  "delete"                       |
  "new"                          |
  "goto"                         |
  "if"                           |
  "switch"                       |
  "return"                       |
  "while"                        |
  "this"                         |
  "try"                          |
  "var"                          |
  "function"                     |
  "with"                         |
  "in"                           |

  /* boolean literals */
  "true"                         |
  "false"                        |

  /* null literal */
  "null"                         { return token(JavascriptToken.KEYWORD); }

  /* Built-in Types*/
  "Array"                        |
  "Boolean"                      |
  "RegExp"                       |
  "String"                       |
  {Identifier} ":"               { return token(JavascriptToken.TYPE); }


  /* operators */

  "("                            |
  ")"                            |
  "{"                            |
  "}"                            |
  "["                            |
  "]"                            |
  ";"                            |
  ","                            |
  "."                            |
  "="                            |
  ">"                            |
  "<"                            |
  "!"                            |
  "~"                            |
  "?"                            |
  ":"                            |
  "=="                           |
  "<="                           |
  ">="                           |
  "!="                           |
  "&&"                           |
  "||"                           |
  "++"                           |
  "--"                           |
  "+"                            |
  "-"                            |
  "*"                            |
  "/"                            |
  "&"                            |
  "|"                            |
  "^"                            |
  "%"                            |
  "<<"                           |
  ">>"                           |
  ">>>"                          |
  "+="                           |
  "-="                           |
  "*="                           |
  "/="                           |
  "&="                           |
  "|="                           |
  "^="                           |
  "%="                           |
  "<<="                          |
  ">>="                          |
  ">>>="                         { return token(JavascriptToken.OPERATOR); }

  /* string literal */
  \"                             {
                                    yybegin(STRING);
                                    tokenStart = yychar;
                                    tokenLength = 1;
                                 }

  /* numeric literals */

  {DecIntegerLiteral}            |
  {DecLongLiteral}               |

  {HexIntegerLiteral}            |
  {HexLongLiteral}               |

  {OctIntegerLiteral}            |
  {OctLongLiteral}               |

  {FloatLiteral}                 |
  {DoubleLiteral}                |
  {DoubleLiteral}[dD]            { return token(JavascriptToken.NUMBER); }

  /* comments */
  {Comment}                      { return token(JavascriptToken.COMMENT); }

  /* whitespace */
  {WhiteSpace}                   { }

  /* identifiers */
  {Identifier}                   { return token(JavascriptToken.IDENTIFIER); }
}

<STRING> {
  \"                             {
                                     yybegin(YYINITIAL);
                                     // length also includes the trailing quote
                                     return token(JavascriptToken.STRING, tokenStart, tokenLength + 1);
                                 }

  {StringCharacter}+             { tokenLength += yylength(); }

  \\[0-3]?{OctDigit}?{OctDigit}  { tokenLength += yylength(); }

  /* escape sequences */

  \\.                            { tokenLength += 2; }
  {LineTerminator}               { yybegin(YYINITIAL);  }
}

/* error fallback */
[^]                              {  }
<<EOF>>                          { return null; }

