package ru.eventflow.fts;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Runner {

    private static EntityManager em =  Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) throws Exception {

        if (args.length < 2 || args.length % 2 != 0 || !(args[0].equals("-d") || args[0].equals("--data"))) {
            System.out.println("Usage:");
            System.out.println("  -d, --data\tdata directory");
            System.exit(1);
        }

        CorpusDataPreprocessor corpusDataPreprocessor = new CorpusDataPreprocessor(em, args[1], true);
        corpusDataPreprocessor.init();
        SearchEngine searchEngine = new SearchEngine(em);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("baseline> ");
            String input = br.readLine();
            if (input == null || input.equals("\\q") || input.equals(":q") || input.equals("exit") ||
                    input.equals("quit") || input.equals("halt.")) {
                break; // EOF
            }

            input = input.trim();
            if (input.equals("")) {
                continue;
            }

            Result result = searchEngine.executeQuery(input);
            System.out.println(result.getDocuments());
        }
    }

}
