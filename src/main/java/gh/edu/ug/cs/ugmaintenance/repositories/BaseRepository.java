package gh.edu.ug.cs.ugmaintenance.repositories;

import java.sql.Connection;
import java.sql.SQLException;

import gh.edu.ug.cs.ugmaintenance.config.DatabaseConnection;

public abstract class BaseRepository {
    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}
