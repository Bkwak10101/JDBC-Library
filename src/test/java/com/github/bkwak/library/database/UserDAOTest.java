package com.github.bkwak.library.database;

import com.github.bkwak.library.model.User;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    final UserDAO userDAO = UserDAO.getInstance();

    @Test
    @DisplayName("should find a user by given login")
    public void findUserByLogin_givenLoginExists() {
        String login = "janusz";
        User user = userDAO.findByLogin(login);
        assertNotNull(user);
        assertEquals(login, user.getLogin());
    }

    @Test
    @DisplayName("should not find a user by given login")
    public void findUserByLogin_givenLoginNotExists() {
        String login = "anna";
        User user = userDAO.findByLogin(login);
        assertNull(user);
    }
}