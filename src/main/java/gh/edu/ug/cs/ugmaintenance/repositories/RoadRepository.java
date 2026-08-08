package gh.edu.ug.cs.ugmaintenance.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.models.Road;
import gh.edu.ug.cs.ugmaintenance.models.enums.RoadCondition;

public class RoadRepository extends BaseRepository implements CrudRepository<Road, Integer> {

    @Override
    public boolean save(Road entity) {
        String sql = """
                INSERT INTO roads
                (from_location_id, to_location_id, distance_km, travel_time_minutes, road_condition, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setInt(1, entity.getFromLocationId());
            statement.setInt(2, entity.getToLocationId());
            statement.setDouble(3, entity.getDistanceKm());
            statement.setInt(4, entity.getTravelTimeMinutes());
            statement.setString(5, entity.getRoadCondition().getDbValue());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setRoadId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Road entity) {
        if (entity.getRoadId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid road ID."
            );
        }

        String sql = """
                UPDATE roads
                SET from_location_id = ?, to_location_id = ?, distance_km = ?, travel_time_minutes = ?, road_condition = ?, created_at = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entity.getFromLocationId());
            statement.setInt(2, entity.getToLocationId());
            statement.setDouble(3, entity.getDistanceKm());
            statement.setInt(4, entity.getTravelTimeMinutes());
            statement.setString(5, entity.getRoadCondition().getDbValue());
            statement.setTimestamp(6, Timestamp.valueOf(entity.getCreatedAt()));
            statement.setInt(7, entity.getRoadId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM roads WHERE id = ?";

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
    public Optional<Road> findById(Integer id) {
        String sql = "SELECT * FROM roads WHERE id = ?";

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
    public List<Road> findAll() {
        String sql = "SELECT * FROM roads";
        List<Road> roads = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                roads.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads;
    }

    public List<Road> findByFromLocation(int fromLocationId) {
        if (fromLocationId <= 0) {
            throw new IllegalArgumentException("Invalid source location ID.");
        }

        String sql = "SELECT * FROM roads WHERE from_location_id = ?";
        List<Road> roads = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, fromLocationId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    roads.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads;
    }

    public List<Road> findByLocation(int locationId) {
        if (locationId <= 0) {
            throw new IllegalArgumentException("Invalid location ID.");
        }

        String sql = "SELECT * FROM roads WHERE from_location_id = ? OR to_location_id = ?";
        List<Road> roads = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, locationId);
            statement.setInt(2, locationId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    roads.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roads;
    }

    private Road mapRow(ResultSet rs) throws SQLException {
        Road road = new Road();
        road.setRoadId(rs.getInt("id"));
        road.setFromLocationId(rs.getInt("from_location_id"));
        road.setToLocationId(rs.getInt("to_location_id"));
        road.setDistanceKm(rs.getDouble("distance_km"));
        road.setTravelTimeMinutes(rs.getInt("travel_time_minutes"));
        road.setRoadCondition(RoadCondition.fromDbValue(rs.getString("road_condition")));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            road.setCreatedAt(createdAt.toLocalDateTime());
        }

        return road;
    }
}
