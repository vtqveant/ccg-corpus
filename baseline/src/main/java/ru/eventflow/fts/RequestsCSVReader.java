package ru.eventflow.fts;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class RequestsCSVReader {

    public static final String REQUESTS_CSV = "/requests.csv";

    public Set<Request> getRequests() throws IOException {
        InputStream in = RequestsCSVReader.class.getResourceAsStream(REQUESTS_CSV);
        Reader reader = new InputStreamReader(in);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withDelimiter(';').withHeader().parse(reader);

        Set<Request> requests = new HashSet<>();
        for (CSVRecord record : records) {
            Request request = new Request(record.get(0), record.get(1));
            requests.add(request);
        }

        return requests;
    }

}
