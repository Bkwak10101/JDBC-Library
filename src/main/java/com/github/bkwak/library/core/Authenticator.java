package com.github.bkwak.library.core;

import com.github.bkwak.library.database.UserDAO;
import com.github.bkwak.library.model.User;
import org.apache.commons.codec.digest.DigestUtils;

public class Authenticator {
    public static final String seed = "$ZhJ253Ro3gE0Fi%@w$@r*kUx46&SIC3A%BbSG%p";
    private User loggedUser = null;
    final UserDAO userDAO = UserDAO.getInstance();
    private static final Authenticator instance = new Authenticator();
    private Authenticator() {

    }

    public static Authenticator getInstance() {
        return instance;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    public String getSeed() {
        return seed;
    }

    public void authenticate(User user) {
        User userFromDB = userDAO.findByLogin(user.getLogin());
        if (userFromDB != null && userFromDB.getPassword().equals(
                DigestUtils.md5Hex(user.getPassword() + seed))) {
            loggedUser = userFromDB;
        }
    }
}
