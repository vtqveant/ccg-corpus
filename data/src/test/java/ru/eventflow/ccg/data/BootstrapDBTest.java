package ru.eventflow.ccg.data;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

public class BootstrapDBTest {

//    @Test
    public void testBootstrapDB() throws IOException {
        Properties properties = new Properties();
        properties.load(ClassLoader.getSystemResourceAsStream("config.properties"));

        String resourcesLocation = properties.getProperty("opencorpora.dump.location");
        if (resourcesLocation != null) {
            new DumpLoader(resourcesLocation).init();
        }
    }
}
