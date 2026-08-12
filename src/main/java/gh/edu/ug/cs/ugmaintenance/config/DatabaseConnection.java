package gh.edu.ug.cs.ugmaintenance.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {

        String host = DatabaseConfig.getProperty("db.host");
        String port = DatabaseConfig.getProperty("db.port");
        String database = DatabaseConfig.getProperty("db.name");

        String username = DatabaseConfig.getProperty("db.username");
        String password = DatabaseConfig.getProperty("db.password");

        boolean ssl =
                Boolean.parseBoolean(
                        DatabaseConfig.getProperty("db.ssl")
                );

        String url =
                "jdbc:mysql://"
                        + host
                        + ":"
                        + port
                        + "/"
                        + database
                        + "?serverTimezone=UTC"
                        + "&sslMode="
                        + (ssl ? "REQUIRED" : "DISABLED");

        return DriverManager.getConnection(url, username, password);
    }

}