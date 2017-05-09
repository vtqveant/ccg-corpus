package ru.eventflow.ccg.annotation.lexer;

import java.io.IOException;
import java.io.Reader;

public interface FlexLexer {

    Token next() throws IOException;

    void reset(Reader reader) throws IOException;
}
