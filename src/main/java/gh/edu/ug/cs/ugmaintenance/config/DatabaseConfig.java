package gh.edu.ug.cs.ugmaintenance.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try {
            loadProperties("application.properties");

            if (properties.isEmpty()) {
                throw new RuntimeException("application.properties not found.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load database configuration.", e);
        }
    }

    private static void loadProperties(String resourceName) throws IOException {
        try (InputStream input = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream(resourceName)) {

            if (input != null) {
                properties.load(input);
            }
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
