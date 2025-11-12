package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.model.User;

import java.sql.*;

public class UserDatabaseDAO implements UserDAO{
    @Override
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Kollar om användaren hittas, skapar User objekt.
                    User user = new User(
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
        e.printStackTrace();
    }
        return null;
    }

    @Override
    public User register(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
