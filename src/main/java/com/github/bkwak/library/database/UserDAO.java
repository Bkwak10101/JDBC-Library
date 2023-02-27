package com.github.bkwak.library.database;

import com.github.bkwak.library.model.User;

import java.sql.*;

public class UserDAO {
    private static final UserDAO instance = new UserDAO();
    private final Connection connection;

    public UserDAO() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library",
                    "root",
                    "");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDAO getInstance() {
        return instance;
    }

    public User findByLogin(String login) {
        try {
            String sql = "SELECT * FROM user WHERE login = ?";
            PreparedStatement ps = this.connection.prepareStatement(sql);

            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role"))
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}