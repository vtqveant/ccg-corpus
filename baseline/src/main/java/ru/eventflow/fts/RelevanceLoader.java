package ru.eventflow.fts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelevanceLoader {

    public static final String SEP = ";";
    public static final int CORPUS_SIZE = 50;

    /**
     * a set of relevant document ids for each request
     */
    public Map<String, Set<Integer>> loadRelevanceData() throws IOException {
        InputStream in = getClass().getResourceAsStream("/relevance.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        Map<String, Set<Integer>> relevance = new HashMap<>();

        // read header and store requests
        String line = reader.readLine();
        String[] requests = line.split(SEP);
        for (int i = 1; i < requests.length; i++) {
            relevance.put(requests[i], new HashSet<Integer>(CORPUS_SIZE));
        }

        // read values
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(SEP);
            Integer id = new Integer(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                if (!parts[i].equals("")) {
                    relevance.get(requests[i]).add(id);
                }
            }
        }

        reader.close();
        in.close();

        return relevance;
    }

}
