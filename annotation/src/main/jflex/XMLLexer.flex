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
%class XMLLexer
%implements FlexLexer
%final

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
        return new XMLToken(id, yychar, yychar + content.length());
    }
%}

%xstate COMMENT, CDATA, TAG, INSTR

/* main character classes */

/* white space */
S = (\u0020 | \u0009 | \u000D | \u000A)+

/* characters */
Char = \u0009 | \u000A | \u000D | [\u0020-\uD7FF] | [\uE000-\uFFFD] | [\u10000-\u10FFFF]

/* comments */
CommentStart = "<!--"
CommentEnd = "-->"

NameStartChar = ":" | [A-Z] | "_" | [a-z]
NameStartCharUnicode = [\u00C0-\u00D6]   |
        [\u00D8-\u00F6] |
        [\u00F8-\u02FF] |
        [\u0370-\u037D] |
        [\u037F-\u1FFF] |
        [\u200C-\u200D] |
        [\u2070-\u218F] |
        [\u2C00-\u2FEF] |
        [\u3001-\uD7FF] |
        [\uF900-\uFDCF] |
        [\uFDF0-\uFFFD] |
        [\u10000-\uEFFFF]

NameChar = {NameStartChar} | "-" | "." | [0-9] | \u00B7
NameCharUnicode = [\u0300-\u036F] | [\u0203F-\u2040]
Name = {NameStartChar} {NameChar}*
NameUnicode = ({NameStartChar}|{NameStartCharUnicode}) ({NameChar}|{NameCharUnicode})*

/* XML Processing Instructions */
InstrStart = "<?" {Name}
InstrEnd   = "?>"

/* CDATA  */
CDataStart = "<![CDATA["
CDataEnd   = "]]>"

/* Tags */
OpenTagStart = "<" {Name}
OpenTagClose = "/>"
OpenTagEnd = ">"

CloseTag = "</" {Name} {S}* ">"

/* attribute */
Attribute = {Name} "="

/* string and character literals */
DQuoteStringChar = [^\r\n\"]
SQuoteStringChar = [^\r\n\']

%%

<YYINITIAL> {
  "&" [a-z]+ ";"                 |
  "&#" [:digit:]+ ";"            { return token(XMLToken.KEYWORD); }

  {InstrStart}                   { yybegin(INSTR); return token(XMLToken.TYPE2); }
  {OpenTagStart}                 { yybegin(TAG); return token(XMLToken.TYPE); }
  {CloseTag}                     { return token(XMLToken.TYPE); }
  {CommentStart}                 { yybegin(COMMENT); return token(XMLToken.COMMENT2); }
  {CDataStart}                   { yybegin(CDATA); return token(XMLToken.COMMENT2); }
}

<INSTR> {
  {Attribute}                    { return token(XMLToken.IDENTIFIER); }

  \"{DQuoteStringChar}*\"        |
  \'{SQuoteStringChar}*\'        { return token(XMLToken.STRING); }

  {InstrEnd}                     { yybegin(YYINITIAL); return token(XMLToken.TYPE2); }
}

<TAG> {
  {Attribute}                    { return token(XMLToken.IDENTIFIER); }

  \"{DQuoteStringChar}*\"        |
  \'{SQuoteStringChar}*\'        { return token(XMLToken.STRING); }


  {OpenTagClose}                 { yybegin(YYINITIAL); return token(XMLToken.TYPE); }
  {OpenTagEnd}                   { yybegin(YYINITIAL); return token(XMLToken.TYPE); }
}

<COMMENT> {
  {CommentEnd}                   { yybegin(YYINITIAL); return token(XMLToken.COMMENT2); }
  ~{CommentEnd}                  { yypushback(3); return token(XMLToken.COMMENT); }
}

<CDATA> {
  {CDataEnd}                     { yybegin(YYINITIAL); return token(XMLToken.COMMENT2); }
  ~{CDataEnd}                    { yypushback(3); return token(XMLToken.COMMENT); }
}

<YYINITIAL,TAG,INSTR,CDATA,COMMENT> {
   [^]                           {  }
   <<EOF>>                       { return null; }
}
