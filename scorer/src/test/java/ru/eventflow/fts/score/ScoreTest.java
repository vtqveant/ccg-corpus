package ru.eventflow.fts.score;

import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ScoreTest {

    private AssessmentCommonsCSVReader assessmentReader = new AssessmentCommonsCSVReader();

    @Test
    public void testComputeScore()  throws IOException {

        Map<String, Set<Integer>> assessments = assessmentReader.getAssessments();
        Scorer scorer = new Scorer(assessments);

        List<Result> results = new ArrayList<>();
        results.add(new Result("экономический кризис", Arrays.asList(10)));
        results.add(new Result("экономический кризис", Arrays.asList(10,7)));
        results.add(new Result("экономический кризис", Arrays.asList(10,236)));
        results.add(new Result("экономический кризис", Arrays.asList(10,7,236,237)));
        results.add(new Result("экономический кризис", Arrays.asList(3)));
        results.add(new Result("экономический кризис", Arrays.asList(3,7)));
        results.add(new Result("экономический кризис", Arrays.asList(3,236)));
        results.add(new Result("экономический кризис", Arrays.asList(3,7,236,237)));
        scorer.evaluateResults(results);

    }
}
