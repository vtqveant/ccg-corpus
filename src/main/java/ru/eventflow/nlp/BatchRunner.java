package ru.eventflow.nlp;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchRunner {

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
            new ResultCSVWriter().write(documentIds, matrix, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
