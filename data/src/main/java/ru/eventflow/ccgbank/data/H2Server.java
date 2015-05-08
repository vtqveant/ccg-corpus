package ru.eventflow.ccgbank.data;

import org.h2.tools.Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class H2Server {

    public static void main(String[] args) throws SQLException {

        try {
            Properties properties = new Properties();
            if (args.length > 0 && args.length % 2 == 0 && args[0].equals("--config")) {
                properties.load(new FileReader(new File(args[1])));
            } else {
                properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));
            }
            String dumpLocation = properties.getProperty("opencorpora.dump.location");
            if (dumpLocation != null) {
                CorpusDumpLoader corpusDumpLoader = new CorpusDumpLoader();
                corpusDumpLoader.init(dumpLocation);
            }
        } catch (IOException e) {
            System.out.println("Premature exit due to misconfiguration");
            System.exit(-1);
        }


        String baseDir = H2Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        Server server = Server.createTcpServer(
                "-tcpPort", "9092",
                "-baseDir", baseDir + "../db",
                "-tcpAllowOthers",
                "-trace"
        );
        server.start();

        System.out.println("Server started at " + server.getURL());
        System.out.println("DB location: " + baseDir + "../db");
        System.out.println("Connection URL: jdbc:h2:tcp://localhost:9092/corpus");
    }
}
