package ru.eventflow.ir;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class BatchRunner {

    public static final String RESULT_CSV = "/tmp/opcorpora/result.csv";

    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) {
        String resourcesLocation = BatchRunner.class.getResource("/gold").getPath();
        SourceProcessor sourceProcessor = new SourceProcessor(em, resourcesLocation, false);
        List<Integer> documentIds = sourceProcessor.init();
        SearchEngine searchEngine = new SearchEngine(em);
        RequestsLoader requestsLoader = new RequestsCSVLoader();
        RelevanceLoader relevanceLoader = new RelevanceLoader();

        try {
            // in
            Map<String, List<Integer>> matrix = new HashMap<>();
            for (String request : requestsLoader.getRequests()) {
                List<Integer> ids = searchEngine.executeQuery(request);
                matrix.put(request, ids);
                System.out.println(request + ": " + ids);
            }


            // compute metrics
            Map<String, Set<Integer>> relevance = relevanceLoader.loadRelevanceData();
            float precision;
            float recall;
            float fmeasure;

            System.out.println();
            System.out.printf("%-30s\t%-5s\t%-5s\t%-5s\n\n", "query", "P", "R", "F1");

            for (String request : matrix.keySet()) {
                Set<Integer> truePositives = new HashSet<Integer>(matrix.get(request));
                truePositives.retainAll(relevance.get(request));

                int truePositivesCount = truePositives.size();
                int totalRelevantCount = relevance.get(request).size();
                int totalRetrievedCount =  matrix.get(request).size();

                try {
                    precision = 100 * truePositivesCount / totalRetrievedCount;
                    recall = 100 * truePositivesCount / totalRelevantCount;
                    fmeasure = 2 * precision * recall / (precision + recall);
                    System.out.printf("%-30s\t%-5s\t%-5s\t%-5s\n", request,  precision, recall, fmeasure);
                } catch (Exception e) {

                }
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
