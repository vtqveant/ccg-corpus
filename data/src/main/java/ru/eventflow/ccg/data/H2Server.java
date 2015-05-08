package ru.eventflow.ccg.data;

import org.h2.tools.Server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class H2Server {

    public static final String PORT = "9092";

    public static void main(String[] args) throws IOException, SQLException {
        Properties properties = new Properties();
        if (args.length > 0 && args.length % 2 == 0 && args[0].equals("--config")) {
            properties.load(new FileReader(new File(args[1])));
        } else {
            properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));
        }
        String databaseLocation = properties.getProperty("opencorpora.database.location");
        if (databaseLocation == null) {
            System.err.println("Exit due to misconfiguration");
            System.exit(-1);
        }

        String path = new File(databaseLocation).getCanonicalPath();

        Server server = Server.createTcpServer(
                "-tcpPort", PORT,
                "-baseDir", path,
                "-tcpAllowOthers",
                "-trace"
        );
        server.start();

        System.out.println("Server started at " + server.getURL());
        System.out.println("DB location: " + path);
        System.out.println("Connection URL: jdbc:h2:tcp://localhost:" + PORT + "/corpus");
    }
}
