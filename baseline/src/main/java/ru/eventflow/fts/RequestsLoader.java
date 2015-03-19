package ru.eventflow.fts;

import java.io.IOException;
import java.util.Set;

public interface RequestsLoader {
    Set<String> getRequests() throws IOException;
}
