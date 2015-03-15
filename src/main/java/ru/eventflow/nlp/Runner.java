package ru.eventflow.nlp;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

public class Runner {

    private static final Logger logger = Logger.getLogger("Runner");

    private static EntityManager em =  Persistence.createEntityManagerFactory("track1").createEntityManager();

    public static void main(String[] args) throws Exception {
        System.out.println(args[0] + ", " + args[1]);

        if (args.length < 2 || args.length % 2 != 0 || !(args[0].equals("-d") || args[0].equals("--data"))) {
            usage();
            System.exit(1);
        }

        new SourceProcessor(em, args[1], true); // TODO print datasource statistics

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

            List<Integer> ids = executeQuery(input);
            System.out.println(ids);
        }
    }

    private static void usage() {
        System.out.println("Usage:");
        System.out.println("\t-d, --data\t\tdata directory");
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> executeQuery(String query) {
        Query q;
        if (query.startsWith("\"") && query.endsWith("\"")) { // if this is an exact match query
            query = query.substring(1, query.length() - 1);
            q = em.createNativeQuery("SELECT id FROM document WHERE text LIKE '%" + query + "%';", Integer.class);
        } else {
            q = em.createNativeQuery("SELECT id FROM document WHERE text @@ plainto_tsquery('" + query + "');", Integer.class);
        }
        return (List<Integer>) q.getResultList();
    }


}
