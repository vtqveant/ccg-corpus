package ru.eventflow.ir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestsCSVLoader implements RequestsLoader {

    private static final String SEP = ";";
    public static final String REQUESTS_CSV = "/requests.csv";

    public Set<String> getRequests() throws IOException {
        Map<String, String> map = parse(REQUESTS_CSV, true);
        return map.keySet();
    }

    private Map<String, String> parse(String resource, boolean skipFirst) throws IOException {
        InputStream in = BatchRunner.class.getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Map<String, String> map = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (skipFirst) {
                skipFirst = false;
                continue;
            }
            int colPos = line.indexOf(SEP);
            String key = line.substring(0, colPos);
            String value = line.substring(colPos);
            map.put(key, value);
        }

        reader.close();
        in.close();

        return map;
    }

}
