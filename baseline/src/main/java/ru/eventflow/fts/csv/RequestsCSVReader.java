package ru.eventflow.fts.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.eventflow.fts.BatchRunner;
import ru.eventflow.fts.Request;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class RequestsCSVReader {

    public static final String REQUESTS_CSV = "/requests.csv";

    public Set<Request> getRequests() throws IOException {
        InputStream in = BatchRunner.class.getResourceAsStream(REQUESTS_CSV);
        Reader reader = new InputStreamReader(in);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').parse(reader);

        Set<Request> requests = new HashSet<>();
        for (CSVRecord record : records) {
            Request request = new Request(record.get(0), record.get(1));
            requests.add(request);
        }

        return requests;
    }

}
