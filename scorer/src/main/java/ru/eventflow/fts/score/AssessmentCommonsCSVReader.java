package ru.eventflow.fts.score;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class AssessmentCommonsCSVReader {

    public static final int CORPUS_SIZE = 50;

    private static final String RELEVANCE_CSV = "/relevance.csv";
    private Map<String, Set<Integer>> assessments = new HashMap<>();

    /**
     * a set of relevant document ids for each request
     * I also have to transpose it
     */
    public Map<String, Set<Integer>> getAssessments() throws IOException {
        InputStream in = AssessmentCommonsCSVReader.class.getResourceAsStream(RELEVANCE_CSV);
        Reader reader = new InputStreamReader(in);
        CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(reader);

        Map<String, Integer> header = parser.getHeaderMap();
        Iterable<CSVRecord> records = parser.getRecords();

        Iterator<Map.Entry<String, Integer>> it = header.entrySet().iterator();
        it.next(); // skip first column

        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            Set<Integer> ids = new HashSet<>(CORPUS_SIZE);
            for (CSVRecord record : records) {
                if (!record.get(entry.getKey()).equals("")) {
                    ids.add(new Integer(record.get(0)));
                }
            }
            assessments.put(entry.getKey(), ids);
        }

        return assessments;
    }

}
