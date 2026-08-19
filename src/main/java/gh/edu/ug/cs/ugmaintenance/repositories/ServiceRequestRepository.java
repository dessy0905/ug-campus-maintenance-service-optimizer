package gh.edu.ug.cs.ugmaintenance.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.datastructures.array.DynamicArray;
import gh.edu.ug.cs.ugmaintenance.datastructures.linkedlist.List;
import gh.edu.ug.cs.ugmaintenance.models.ServiceRequest;
import gh.edu.ug.cs.ugmaintenance.models.enums.RequestStatus;

public class ServiceRequestRepository
        extends BaseRepository
        implements CrudRepository<ServiceRequest, Integer> {

    @Override
    public boolean save(ServiceRequest request) {
        String sql = """
                INSERT INTO service_requests
                (
                    user_id,
                    location_id,
                    category_id,
                    request_title,
                    description,
                    urgency_level,
                    status,
                    request_date,
                    completion_date
                )
                VALUES
                (?,?,?,?,?,?,?,?,?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, request.getUserId());
            statement.setInt(2, request.getLocationId());
            statement.setInt(3, request.getCategoryId());
            statement.setString(4, request.getRequestTitle());
            statement.setString(5, request.getDescription());
            statement.setInt(6, request.getUrgencyLevel());
            statement.setString(7, request.getStatus().getDbValue());
            statement.setTimestamp(8, Timestamp.valueOf(request.getRequestDate()));

            if (request.getCompletionDate() == null) {
                statement.setNull(9, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(9, Timestamp.valueOf(request.getCompletionDate()));
            }

            int rowsAffected = statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    request.setRequestId(keys.getInt(1));
                }
            }

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ServiceRequest request) {
        if (request.getRequestId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid request ID."
            );
        }

        String sql = """
                UPDATE service_requests
                SET
                    user_id = ?,
                    location_id = ?,
                    category_id = ?,
                    request_title = ?,
                    description = ?,
                    urgency_level = ?,
                    status = ?,
                    request_date = ?,
                    completion_date = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, request.getUserId());
            statement.setInt(2, request.getLocationId());
            statement.setInt(3, request.getCategoryId());
            statement.setString(4, request.getRequestTitle());
            statement.setString(5, request.getDescription());
            statement.setInt(6, request.getUrgencyLevel());
            statement.setString(7, request.getStatus().getDbValue());
            statement.setTimestamp(8, Timestamp.valueOf(request.getRequestDate()));

            if (request.getCompletionDate() == null) {
                statement.setNull(9, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(9, Timestamp.valueOf(request.getCompletionDate()));
            }

            statement.setInt(10, request.getRequestId());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM service_requests WHERE id = ?";

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
    public Optional<ServiceRequest> findById(Integer id) {
        String sql = "SELECT * FROM service_requests WHERE id = ?";

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
    public List<ServiceRequest> findAll() {
        String sql = "SELECT * FROM service_requests";
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                requests.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<ServiceRequest> findByUserId(int userId) {
        String sql = """
                SELECT * FROM service_requests
                WHERE user_id = ?
                ORDER BY request_date DESC
                """;
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public boolean updateStatus(int requestId, RequestStatus status) {
        String sql = "UPDATE service_requests SET status = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.getDbValue());
            statement.setInt(2, requestId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ServiceRequest> findPendingRequests() {
        return findByStatus(RequestStatus.PENDING);
    }

    public List<ServiceRequest> findCompletedRequests() {
        return findByStatus(RequestStatus.COMPLETED);
    }

    public List<ServiceRequest> findByStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        String sql = "SELECT * FROM service_requests WHERE status = ? ORDER BY request_date DESC";
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status.getDbValue());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<ServiceRequest> findByPriority(int priority) {
        if (priority < 1 || priority > 5) {
            throw new IllegalArgumentException("Priority must be between 1 and 5.");
        }

        String sql = "SELECT * FROM service_requests WHERE urgency_level = ? ORDER BY request_date DESC";
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, priority);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<ServiceRequest> findByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID.");
        }

        String sql = "SELECT * FROM service_requests WHERE category_id = ? ORDER BY request_date DESC";
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public List<ServiceRequest> findByLocation(int locationId) {
        if (locationId <= 0) {
            throw new IllegalArgumentException("Invalid location ID.");
        }

        String sql = "SELECT * FROM service_requests WHERE location_id = ? ORDER BY request_date DESC";
        List<ServiceRequest> requests = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, locationId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requests;
    }

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        ServiceRequest request = new ServiceRequest();

        request.setRequestId(rs.getInt("id"));
        request.setUserId(rs.getInt("user_id"));
        request.setLocationId(rs.getInt("location_id"));
        request.setCategoryId(rs.getInt("category_id"));
        request.setRequestTitle(rs.getString("request_title"));
        request.setDescription(rs.getString("description"));
        request.setUrgencyLevel(parseUrgencyLevel(rs));
        request.setStatus(RequestStatus.fromDbValue(rs.getString("status")));

        var requestDate = rs.getTimestamp("request_date");
        if (requestDate != null) {
            request.setRequestDate(requestDate.toLocalDateTime());
        }

        var completionDate = rs.getTimestamp("completion_date");
        if (completionDate != null) {
            request.setCompletionDate(completionDate.toLocalDateTime());
        }

        return request;
    }

    private int parseUrgencyLevel(ResultSet rs) throws SQLException {
        Object value = rs.getObject("urgency_level");

        if (value == null) {
            return 1;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        String text = value.toString().trim();

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException ignored) {
            return switch (text.toLowerCase()) {
                case "low", "lowest" -> 1;
                case "medium", "moderate" -> 3;
                case "high" -> 4;
                case "critical", "urgent", "highest" -> 5;
                default -> 3;
            };
        }
    }
}
