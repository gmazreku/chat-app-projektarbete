package se.sprinto.hakan.chatapp.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.sprinto.hakan.chatapp.model.Message;
import se.sprinto.hakan.chatapp.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageDBTestDao {
    @BeforeEach
    void setup() throws SQLException {
        Connection conn = DatabaseUtil.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(100))");
        stmt.execute("CREATE TABLE messages (id INT PRIMARY KEY AUTO_INCREMENT, message TEXT, timestamp DATETIME, user_id INT, FOREIGN KEY (user_id) REFERENCES users(id))");

        stmt.close();
    }
    @Test
    void testMessage(){
        UserDatabaseDAO userDAO = new UserDatabaseDAO();
        MessageDatabaseDAO messageDAO = new MessageDatabaseDAO();

        User user = new User("test1", "test2");
        user = userDAO.register(user);

        Message message1 = new Message(user.getId(), "Test 1", LocalDateTime.now());
        Message message2 = new Message(user.getId(), "Test 2", LocalDateTime.now());

        messageDAO.saveMessage(message1);
        messageDAO.saveMessage(message2);

        List<Message> messages = messageDAO.getMessagesByUserId(user.getId());

        assertEquals(2, messages.size());
    }
}
