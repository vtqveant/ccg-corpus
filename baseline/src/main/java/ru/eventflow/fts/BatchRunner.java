package ru.eventflow.fts;

import ru.eventflow.fts.csv.RequestsCSVReader;
import ru.eventflow.fts.csv.ResultCSVWriter;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class BatchRunner {

    public static final String RESULT_CSV = "/tmp/opcorpora/result.csv";

    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) {
        String resourcesLocation = BatchRunner.class.getResource("/gold").getPath();
        CorpusDataPreprocessor corpusDataPreprocessor = new CorpusDataPreprocessor(em, resourcesLocation, false);
        List<Integer> documentIds = corpusDataPreprocessor.init();
        SearchEngine searchEngine = new SearchEngine(em);
        RequestsCSVReader requestsCSVReader = new RequestsCSVReader();

        try {
            // in
            List<Result> results = new ArrayList<>();
            for (Request request : requestsCSVReader.getRequests()) {
                Result result = searchEngine.executeQuery(request.getText());
                results.add(result);
                System.out.println(request + ": " + result.getDocuments());
            }

            // compute metrics
//            Scorer.computeScore(results);

            // out
            File file = new File(RESULT_CSV);
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            new ResultCSVWriter().write(documentIds, results, new OutputStreamWriter(out));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
