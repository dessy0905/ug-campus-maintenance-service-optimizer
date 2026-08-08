package gh.edu.ug.cs.ugmaintenance.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gh.edu.ug.cs.ugmaintenance.models.ServiceCategory;

public class ServiceCategoryRepository extends BaseRepository implements CrudRepository<ServiceCategory, Integer> {

    @Override
    public boolean save(ServiceCategory entity) {
        String sql = """
                INSERT INTO service_categories
                (category_name, description)
                VALUES (?, ?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, entity.getCategoryName());
            statement.setString(2, entity.getDescription());
            int rowsAffected = statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setCategoryId(keys.getInt(1));
                }
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ServiceCategory entity) {
        if (entity.getCategoryId() <= 0) {
            throw new IllegalArgumentException(
                    "Invalid category ID."
            );
        }

        String sql = """
                UPDATE service_categories
                SET category_name = ?, description = ?
                WHERE id = ?
                """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getCategoryName());
            statement.setString(2, entity.getDescription());
            statement.setInt(3, entity.getCategoryId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM service_categories WHERE id = ?";

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
    public Optional<ServiceCategory> findById(Integer id) {
        String sql = "SELECT * FROM service_categories WHERE id = ?";

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
    public List<ServiceCategory> findAll() {
        String sql = "SELECT * FROM service_categories";
        List<ServiceCategory> categories = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                categories.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private ServiceCategory mapRow(ResultSet rs) throws SQLException {
        ServiceCategory category = new ServiceCategory();
        category.setCategoryId(rs.getInt("id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        return category;
    }
}
