package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDatabaseDAO implements MessageDAO{
    @Override
    public void saveMessage(Message message) {
        String sql = "INSERT INTO messages (message, timestamp, user_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, message.getText());
            stmt.setTimestamp(2, Timestamp.valueOf(message.getTimestamp()));
            stmt.setInt(3, message.getUserId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        String sql = "SELECT * FROM messages WHERE user_id = ?";
        List<Message> messages = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message(
                            rs.getInt("user_id"),
                            rs.getString("message"),
                            rs.getTimestamp("timestamp").toLocalDateTime()
                    );
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
