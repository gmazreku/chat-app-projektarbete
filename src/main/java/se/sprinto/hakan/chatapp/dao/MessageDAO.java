package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.model.Message;

import java.util.List;

public interface MessageDAO {
    void saveMessage(Message message);

    List<Message> getMessagesByUserId(int userId);
}
