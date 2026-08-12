package gh.edu.ug.cs.ugmaintenance.config;

import java.sql.Connection;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("✓ Connected to Aiven MySQL successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
