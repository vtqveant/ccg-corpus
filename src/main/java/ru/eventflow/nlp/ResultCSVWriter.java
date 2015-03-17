package ru.eventflow.nlp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class ResultCSVWriter implements ResultWriter {

    public static final char SEP = ';';

    public void write(List<Integer> documentIds, Map<String, List<Integer>> matrix,
                      OutputStream out) throws IOException {
        StringBuilder sb = new StringBuilder();

        // header
        for (Integer id : documentIds) {
            sb.append(SEP);
            sb.append(id);
        }
        sb.append('\n');

        // rows
        for (Map.Entry<String, List<Integer>> entry : matrix.entrySet()) {
            sb.append(entry.getKey());
            List<Integer> evaluations = entry.getValue();

            int i = 0;
            int lastEvalIdx = (evaluations.size() > 0 ) ? evaluations.get(evaluations.size() - 1) : -1;
            for (Integer docId : documentIds) {
                sb.append(SEP);
                if (docId > lastEvalIdx || docId < evaluations.get(i)) {
                    sb.append('f');
                } else {
                    sb.append('t');
                    i++;
                }
            }
            sb.append('\n');
        }

        out.write(sb.toString().getBytes());
    }

}
