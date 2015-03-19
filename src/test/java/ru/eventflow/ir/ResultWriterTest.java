package ru.eventflow.ir;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultWriterTest {

    @Test
    public void testResultsCSVWriter() throws IOException {
        List<Integer> docIds = Arrays.asList(1,2,3,4,5);
        Map<String, List<Integer>> matrix = new HashMap<>();
        matrix.put("a", Arrays.asList(1));
        matrix.put("b", Arrays.asList(2));
        matrix.put("c", Arrays.asList(3));
        matrix.put("d", Arrays.asList(4));
        matrix.put("e", Arrays.asList(5));

        // a matrix with t's on the diagonal in CSV format is expected
        ResultWriter writer = new ResultCSVWriter();
        writer.write(docIds, matrix, System.out);
    }

}
