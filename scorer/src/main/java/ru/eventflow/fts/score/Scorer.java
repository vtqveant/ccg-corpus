package ru.eventflow.fts.score;

import java.io.IOException;
import java.util.*;

public class Scorer {

    Map<String, Set<Integer>> assessments = new HashMap<>();

    public Scorer(Map<String, Set<Integer>> assessments) {
        this.assessments = assessments;
    }

    public void evaluateResults(List<Result> results) throws IOException {
        float precision;
        float recall;
        float fmeasure;

        System.out.println();
        System.out.printf("%-30s\t%-5s\t%-5s\t%-5s\n\n", "query", "P", "R", "F1");

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
                System.out.printf("%-30s\t%-5s\t%-5s\t%-5s\n", request, precision, recall, fmeasure);
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
                "/results/blavachinskaya et al.csv"
        };

        for (String resource : resources) {
            List<Result> results = new ResultsCSVReader().getRequests(resource);
            System.out.println(resource);
            scorer.evaluateResults(results);
            System.out.println();
        }
    }
}
