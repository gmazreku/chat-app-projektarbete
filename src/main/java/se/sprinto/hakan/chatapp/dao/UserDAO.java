package se.sprinto.hakan.chatapp.dao;

import se.sprinto.hakan.chatapp.model.User;

public interface UserDAO {
    User login(String username, String password);

    User register(User user);
}
