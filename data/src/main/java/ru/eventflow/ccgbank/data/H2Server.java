package ru.eventflow.ccgbank.data;

import org.h2.tools.Server;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class H2Server {

    public static final String PORT = "9092";

    public static void main(String[] args) throws IOException, SQLException {
        String baseDir = H2Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = new File(baseDir + "../db").getCanonicalPath();

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
