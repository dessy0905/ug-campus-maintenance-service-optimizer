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
import gh.edu.ug.cs.ugmaintenance.models.Technician;

public class TechnicianRepository extends BaseRepository implements CrudRepository<Technician, Integer> {

    @Override
    public boolean save(Technician entity) {
        String sql = """
                INSERT INTO technicians
                (full_name, specialization, category_id, phone_number, vehicle_assigned, availability_status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getSpecialization());
            statement.setInt(3, entity.getCategoryId());
            statement.setString(4, entity.getPhoneNumber());
            statement.setString(5, entity.getVehicleAssigned());
            statement.setBoolean(6, entity.isAvailabilityStatus());
            statement.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt()));

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setTechnicianId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Technician entity) {
        if (entity.getTechnicianId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid technician ID."
            );
        }

        String sql = """
                UPDATE technicians
                SET full_name = ?, specialization = ?, category_id = ?, phone_number = ?, vehicle_assigned = ?, availability_status = ?, created_at = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getSpecialization());
            statement.setInt(3, entity.getCategoryId());
            statement.setString(4, entity.getPhoneNumber());
            statement.setString(5, entity.getVehicleAssigned());
            statement.setBoolean(6, entity.isAvailabilityStatus());
            statement.setTimestamp(7, Timestamp.valueOf(entity.getCreatedAt()));
            statement.setInt(8, entity.getTechnicianId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM technicians WHERE id = ?";

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
    public Optional<Technician> findById(Integer id) {
        String sql = "SELECT * FROM technicians WHERE id = ?";

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
    public List<Technician> findAll() {
        String sql = "SELECT * FROM technicians";
        List<Technician> technicians = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                technicians.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicians;
    }

    public List<Technician> findAvailableByCategory(int categoryId) {
        String sql = "SELECT * FROM technicians WHERE category_id = ? AND availability_status = true";
        List<Technician> technicians = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicians;
    }

    public List<Technician> findAvailableTechnicians() {
        String sql = "SELECT * FROM technicians WHERE availability_status = true";
        List<Technician> technicians = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                technicians.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicians;
    }

    public List<Technician> findByCategory(int categoryId) {
        String sql = "SELECT * FROM technicians WHERE category_id = ?";
        List<Technician> technicians = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    technicians.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technicians;
    }

    public boolean updateAvailability(int technicianId, boolean available) {
        String sql = "UPDATE technicians SET availability_status = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, technicianId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Technician mapRow(ResultSet rs) throws SQLException {
        Technician technician = new Technician();
        technician.setTechnicianId(rs.getInt("id"));
        technician.setFullName(rs.getString("full_name"));
        technician.setSpecialization(rs.getString("specialization"));
        technician.setCategoryId(rs.getInt("category_id"));
        technician.setLocationId(rs.getInt("location_id"));
        technician.setPhoneNumber(rs.getString("phone_number"));
        technician.setVehicleAssigned(rs.getString("vehicle_assigned"));
        technician.setAvailabilityStatus(rs.getBoolean("availability_status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            technician.setCreatedAt(createdAt.toLocalDateTime());
        }

        return technician;
    }
}
