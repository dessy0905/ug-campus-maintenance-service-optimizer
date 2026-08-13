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
import gh.edu.ug.cs.ugmaintenance.models.User;
import gh.edu.ug.cs.ugmaintenance.models.enums.UserRole;

public class CampusUserRepository extends BaseRepository implements CrudRepository<User, Integer> {

    @Override
    public boolean save(User entity) {
        String sql = """
                INSERT INTO campus_users
                (full_name, email, phone_number, role, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getRole().getDbValue());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));

            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setUserId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User entity) {
        if (entity.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        String sql = """
                UPDATE campus_users
                SET full_name = ?, email = ?, phone_number = ?, role = ?, created_at = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getRole().getDbValue());
            statement.setTimestamp(5, Timestamp.valueOf(entity.getCreatedAt()));
            statement.setInt(6, entity.getUserId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM campus_users WHERE id = ?";

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
    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM campus_users WHERE id = ?";

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
    public List<User> findAll() {
        String sql = "SELECT * FROM campus_users";
        List<User> users = new DynamicArray<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRole(UserRole.fromDbValue(rs.getString("role")));

        String fullName = rs.getString("full_name");
        user.setFirstName(fullName != null ? fullName.split(" ")[0] : "");
        user.setLastName(fullName != null && fullName.split(" ").length > 1 ? fullName.split(" ")[1] : "");

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }

        return user;
    }
}
