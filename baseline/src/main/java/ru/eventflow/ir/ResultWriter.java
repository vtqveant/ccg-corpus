package ru.eventflow.ir;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ResultWriter {

    void write(List<Integer> documentIds, Map<String, List<Integer>> matrix,
               OutputStream out) throws IOException;
}
