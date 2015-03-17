package ru.eventflow.nlp;

import java.io.IOException;
import java.util.Set;

public interface RequestsLoader {
    Set<String> getRequests() throws IOException;
}
