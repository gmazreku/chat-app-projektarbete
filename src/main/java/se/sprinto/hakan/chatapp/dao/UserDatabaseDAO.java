package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;

import java.sql.*;

public class UserDatabaseDAO implements UserDAO{
    @Override
    public User login(String username, String password) {
        String sql = "SELECT u.id, u.username, u.password, m.id AS message_id, m.message, m.timestamp, m.user_id " +
                     "FROM users u " +
                     "LEFT JOIN messages m ON u.id = m.user_id " +
                     "WHERE u.username =? AND u.password=?";

        User user = null;

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (user == null) {
                        user = new User(
                                rs.getString("username"),
                                rs.getString("password")
                        );
                        user.setId(rs.getInt("id"));
                    }

                    if (rs.getString("message") != null) {
                        Message message = new Message(
                                rs.getInt("user_id"),
                                rs.getString("message"),
                                rs.getTimestamp("timestamp").toLocalDateTime()
                        );
                        user.addMessage(message);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
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
