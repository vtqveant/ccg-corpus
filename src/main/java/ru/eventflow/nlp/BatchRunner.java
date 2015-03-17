package ru.eventflow.nlp;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class BatchRunner {

    public static final char SEP = ';';
    public static final String RESULT_CSV = "/tmp/opcorpora/result.csv";

    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) {
        String resourcesLocation = BatchRunner.class.getResource("/gold").getPath();
        SourceProcessor sourceProcessor = new SourceProcessor(em, resourcesLocation, false);
        List<Integer> documentIds = sourceProcessor.init();
        SearchEngine searchEngine = new SearchEngine(em);
        RequestsLoader requestsLoader = new RequestsCSVLoader();

        try {
            // in
            Map<String, List<Integer>> matrix = new HashMap<>();
            for (String request : requestsLoader.getRequests()) {
                List<Integer> ids = searchEngine.executeQuery(request);
                matrix.put(request, ids);
                System.out.println(request + ": " + ids);
            }

            // out
            File file = new File(RESULT_CSV);
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            flushMatrix(documentIds, matrix, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void flushMatrix(List<Integer> documentIds, Map<String, List<Integer>> matrix,
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
