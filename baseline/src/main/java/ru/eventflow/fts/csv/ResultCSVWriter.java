package ru.eventflow.fts.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ru.eventflow.fts.Result;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

public class ResultCSVWriter {

    public void write(List<Integer> documentIds, List<Result> results,
                      OutputStreamWriter out) throws IOException {

        Collections.sort(documentIds);

        String[] header = buildHeader(documentIds);
        final CSVPrinter printer = CSVFormat.DEFAULT.withHeader(header).print(out);

        // rows
        for (Result result : results) {
            printer.print(result.getRequest());
            List<Integer> evaluations = result.getDocuments();
            int i = 0;
            int lastEvalIdx = (evaluations.size() > 0) ? evaluations.get(evaluations.size() - 1) : -1;
            for (Integer docId : documentIds) {
                if (docId > lastEvalIdx || docId < evaluations.get(i)) {
                    printer.print('f');
                } else {
                    printer.print('t');
                    i++;
                }
            }
            printer.println();
        }
        printer.close();
    }

    private String[] buildHeader(List<Integer> ids) {
        String[] strings = new String[ids.size() + 1];
        strings[0] = "";
        for (int i = 0; i < ids.size(); i++) {
            strings[i + 1] = ids.get(i).toString();
        }
        return strings;
    }

}
