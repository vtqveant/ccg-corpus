package ru.eventflow.fts.score;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ResultCSVReaderTest {

    @Test
    public void testReader() throws IOException {
        ResultsCSVReader reader = new ResultsCSVReader();
        List<Result> results = reader.getRequests("/results/blavachinskaya et al.csv");
        System.out.println(results.size());
    }
}
