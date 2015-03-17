package ru.eventflow.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CSVReader {

    private static final String SEPARATOR = ";";

    public Set<String> getRequests() throws IOException {
        Map<String, String[]> map = parse("/requests.csv", true);
        return map.keySet();
    }

    public Map<String, String[]> parse(String resource, boolean skipFirst) throws IOException {
        InputStream in = BatchRunner.class.getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Map<String, String[]> map = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            int colPos = line.indexOf(SEPARATOR);
            String key = line.substring(0, colPos);
            String values = line.substring(colPos);
            String[] parts = values.split(SEPARATOR);
            map.put(key, parts);
        }

        reader.close();
        in.close();

        return map;
    }

}
