package ru.eventflow.fts;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class ResultWriterTest {

    @Test
    public void testResultsCSVWriter() throws IOException {
        List<Integer> docIds = Arrays.asList(1, 2, 3, 4, 5);
        Result[] results = new Result[]{
                new Result("a", Arrays.asList(1)),
                new Result("b", Arrays.asList(2)),
                new Result("c", Arrays.asList(3)),
                new Result("d", Arrays.asList(4)),
                new Result("e", Arrays.asList(5)),
        };

        // a matrix with t's on the diagonal in CSV format is expected
        ResultCSVWriter writer = new ResultCSVWriter();
        writer.write(docIds, Arrays.asList(results), new OutputStreamWriter(System.out));
    }

}
