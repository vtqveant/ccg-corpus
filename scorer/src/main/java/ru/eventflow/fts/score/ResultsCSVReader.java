package ru.eventflow.fts.score;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsCSVReader {

    public List<Result> getRequests(String resource) throws IOException {
        InputStream in = getClass().getResourceAsStream(resource);
        Reader reader = new InputStreamReader(in);
        CSVParser parser = CSVFormat.EXCEL.withDelimiter(';').withHeader().parse(reader);

        List<CSVRecord> records = parser.getRecords();
        Map<String, Integer> header = parser.getHeaderMap();

        List<Result> results = new ArrayList<>(records.size() - 1);
        for (CSVRecord record : records) {
            boolean first = true;
            String request = record.get(0);
            List<Integer> ids = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : header.entrySet()) {
                if (first) {
                    first = false;
                    continue;
                }
                String id = entry.getKey();
                if (!record.get(id).equals("0")) {
                    ids.add(Integer.parseInt(id));
                }
            }
            results.add(new Result(request, ids));
        }

        return results;
    }
}
