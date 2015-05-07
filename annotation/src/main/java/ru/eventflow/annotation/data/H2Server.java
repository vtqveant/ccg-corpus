package ru.eventflow.annotation.data;

import org.h2.tools.Server;
import java.sql.SQLException;

public class H2Server {

    public static void main(String[] args) throws SQLException {
        String baseDir = H2Server.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        Server server = Server.createTcpServer(
                "-tcpPort", "9092",
                "-baseDir", baseDir + "../db",
                "-tcpAllowOthers",
                "-trace"
        );
        server.start();

        System.out.println("server started at " + server.getURL());
        System.out.println("db location is " + baseDir);
        System.out.println("connection url is jdbc:h2:tcp://localhost:9092/corpus");
    }
}
