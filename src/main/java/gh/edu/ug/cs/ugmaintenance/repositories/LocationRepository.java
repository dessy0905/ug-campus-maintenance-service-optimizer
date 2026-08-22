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
import gh.edu.ug.cs.ugmaintenance.models.Location;
import gh.edu.ug.cs.ugmaintenance.models.enums.LocationType;

public class LocationRepository extends BaseRepository implements CrudRepository<Location, Integer> {

    @Override
    public boolean save(Location entity) {
        String sql = """
                INSERT INTO locations
                (location_name, location_type, description, created_at)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, entity.getLocationName());
            statement.setString(2, entity.getLocationType().getDbValue());
            statement.setString(3, entity.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setLocationId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Location entity) {
        String sql = """
                UPDATE locations
                SET location_name = ?, location_type = ?, description = ?, created_at = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getLocationName());
            statement.setString(2, entity.getLocationType().getDbValue());
            statement.setString(3, entity.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getCreatedAt()));
            statement.setInt(5, entity.getLocationId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM locations WHERE id = ?";

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
    public Optional<Location> findById(Integer id) {
        String sql = "SELECT * FROM locations WHERE id = ?";

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
    public List<Location> findAll() {
        String sql = "SELECT * FROM locations";
        List<Location> locations = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public Optional<Location> findByName(String locationName) {
        if (locationName == null || locationName.isBlank()) {
            return Optional.empty();
        }

        String sql = """
                SELECT * FROM locations
                WHERE LOWER(location_name) = LOWER(?)
                   OR LOWER(location_name) LIKE LOWER(?)
                ORDER BY CASE
                    WHEN LOWER(location_name) = LOWER(?) THEN 0
                    ELSE 1
                END, id
                LIMIT 1
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            String trimmed = locationName.trim();
            statement.setString(1, trimmed);
            statement.setString(2, "%" + trimmed + "%");
            statement.setString(3, trimmed);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private Location mapRow(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setLocationId(rs.getInt("id"));
        location.setLocationName(rs.getString("location_name"));
        location.setLocationType(LocationType.fromDbValue(rs.getString("location_type")));
        location.setDescription(rs.getString("description"));
        location.setXCoordinate(rs.getDouble("x_coordinate"));
        location.setYCoordinate(rs.getDouble("y_coordinate"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            location.setCreatedAt(createdAt.toLocalDateTime());
        }

        return location;
    }
}
