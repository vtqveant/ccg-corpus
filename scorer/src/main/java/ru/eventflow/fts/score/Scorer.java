package ru.eventflow.fts.score;

import java.io.IOException;
import java.util.*;

public class Scorer {

    public static final String FORMAT = "%-40s\t%-5s\t%-5s\t%-5s\n";
    Map<String, Set<Integer>> assessments = new HashMap<>();

    public Scorer(Map<String, Set<Integer>> assessments) {
        this.assessments = assessments;
    }

    public void evaluateResults(List<Result> results) throws IOException {
        float precision;
        float recall;
        float fmeasure;

        Collections.sort(results);

        System.out.printf(FORMAT, "query", "P", "R", "F1");
        System.out.println();

        for (Result result : results) {
            String request = result.getRequest();
            Set<Integer> truePositives = new HashSet<Integer>(result.getDocuments());
            truePositives.retainAll(assessments.get(request));

            int truePositivesCount = truePositives.size();
            int totalRelevantCount = assessments.get(request).size();
            int totalRetrievedCount = result.getDocuments().size();

            try {
                precision = 100 * truePositivesCount / totalRetrievedCount;
                recall = 100 * truePositivesCount / totalRelevantCount;
                fmeasure = 2 * precision * recall / (precision + recall);
                System.out.printf(FORMAT, request, precision, recall, fmeasure);
            } catch (Exception e) {
                // ignore
            }
        }

    }

    public static void main(String[] args) throws IOException {
        final AssessmentCommonsCSVReader assessmentReader = new AssessmentCommonsCSVReader();
        Map<String, Set<Integer>> assessments = assessmentReader.getAssessments();
        Scorer scorer = new Scorer(assessments);

        String[] resources = new String[] {
                "/results/baseline.csv",
                "/results/baseline_no_stemming.csv",
                "/results/blavachinskaya et al.csv",
                "/results/invalid.csv"
        };

        for (String resource : resources) {
            List<Result> results = new ResultsCSVReader().getRequests(resource);
            System.out.println(resource);
            scorer.evaluateResults(results);
            System.out.println();
        }
    }
}
