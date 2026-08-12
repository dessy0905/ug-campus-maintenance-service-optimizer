package gh.edu.ug.cs.ugmaintenance.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.RequestStatusLog;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class RequestStatusLogRepository extends BaseRepository implements CrudRepository<RequestStatusLog, Integer> {

    @Override
    public boolean save(RequestStatusLog entity) {
        String sql = """
                INSERT INTO request_status_logs
                (request_id, old_status, new_status, updated_by, comments, updated_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, entity.getRequestId());
            statement.setString(2, entity.getOldStatus() == null ? null : entity.getOldStatus().getDbValue());
            statement.setString(3, entity.getNewStatus().getDbValue());
            statement.setInt(4, entity.getUpdatedBy());
            statement.setString(5, entity.getComments());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setLogId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(RequestStatusLog entity) {
        if (entity.getLogId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid log ID."
            );
        }

        String sql = """
                UPDATE request_status_logs
                SET request_id = ?, old_status = ?, new_status = ?, updated_by = ?, comments = ?, updated_at = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entity.getRequestId());
            statement.setString(2, entity.getOldStatus() == null ? null : entity.getOldStatus().getDbValue());
            statement.setString(3, entity.getNewStatus().getDbValue());
            statement.setInt(4, entity.getUpdatedBy());
            statement.setString(5, entity.getComments());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getUpdatedAt()));
            statement.setInt(7, entity.getLogId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM request_status_logs WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<RequestStatusLog> findById(Integer id) {
        String sql = "SELECT * FROM request_status_logs WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<RequestStatusLog> findAll() {
        String sql = "SELECT * FROM request_status_logs";
        List<RequestStatusLog> logs = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                logs.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    private RequestStatusLog mapRow(ResultSet rs) throws SQLException {
        RequestStatusLog log = new RequestStatusLog();
        log.setLogId(rs.getInt("id"));
        log.setRequestId(rs.getInt("request_id"));
        log.setOldStatus(RequestStatus.fromDbValue(rs.getString("old_status")));
        log.setNewStatus(RequestStatus.fromDbValue(rs.getString("new_status")));
        log.setUpdatedBy(rs.getInt("updated_by"));
        log.setComments(rs.getString("comments"));

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            log.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return log;
    }
}
