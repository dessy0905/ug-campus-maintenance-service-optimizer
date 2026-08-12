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
import gh.edu.ug.cs.ugmaintenance.models.TechnicianAssignment;
import gh.edu.ug.cs.ugmaintenance.models.enums.AssignmentStatus;

public class TechnicianAssignmentRepository extends BaseRepository implements CrudRepository<TechnicianAssignment, Integer> {

    @Override
    public boolean save(TechnicianAssignment entity) {
        String sql = """
                INSERT INTO technician_assignments
                (request_id, technician_id, assigned_date, assignment_status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, entity.getRequestId());
            statement.setInt(2, entity.getTechnicianId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getAssignedDate()));
            statement.setString(4, entity.getAssignmentStatus().getDbValue());

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setAssignmentId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(TechnicianAssignment entity) {
        if (entity.getAssignmentId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid assignment ID."
            );
        }

        String sql = """
                UPDATE technician_assignments
                SET request_id = ?, technician_id = ?, assigned_date = ?, assignment_status = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entity.getRequestId());
            statement.setInt(2, entity.getTechnicianId());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getAssignedDate()));
            statement.setString(4, entity.getAssignmentStatus().getDbValue());
            statement.setInt(5, entity.getAssignmentId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM technician_assignments WHERE id = ?";

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
    public Optional<TechnicianAssignment> findById(Integer id) {
        String sql = "SELECT * FROM technician_assignments WHERE id = ?";

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
    public List<TechnicianAssignment> findAll() {
        String sql = "SELECT * FROM technician_assignments";
        List<TechnicianAssignment> assignments = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                assignments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assignments;
    }

    public boolean updateStatus(int assignmentId, AssignmentStatus status) {
        String sql = "UPDATE technician_assignments SET assignment_status = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status.getDbValue());
            statement.setInt(2, assignmentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private TechnicianAssignment mapRow(ResultSet rs) throws SQLException {
        TechnicianAssignment assignment = new TechnicianAssignment();
        assignment.setAssignmentId(rs.getInt("id"));
        assignment.setRequestId(rs.getInt("request_id"));
        assignment.setTechnicianId(rs.getInt("technician_id"));
        assignment.setAssignmentStatus(AssignmentStatus.fromDbValue(rs.getString("assignment_status")));

        Timestamp assignedAt = rs.getTimestamp("assigned_date");
        if (assignedAt != null) {
            assignment.setAssignedDate(assignedAt.toLocalDateTime());
        }

        return assignment;
    }
}
