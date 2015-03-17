package ru.eventflow.nlp;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

public class BatchRunner {

    private static EntityManager em = Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) {
        String resourcesLocation = BatchRunner.class.getResource("/gold").getPath();
        new SourceProcessor(em, resourcesLocation, true);
        Processor processor = new Processor(em);

        CSVReader csvReader = new CSVReader();
        try {
            for (String request : csvReader.getRequests()) {
                List<Integer> ids = processor.executeQuery(request);
                System.out.println(request + ": " + ids);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
