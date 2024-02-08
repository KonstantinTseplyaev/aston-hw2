package com.aston.hw2.dao;

import com.aston.hw2.dao.config.JdbcConfig;
import com.aston.hw2.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.aston.hw2.dao.util.RequestUtil.ADD_USER;
import static com.aston.hw2.dao.util.RequestUtil.FIND_USER_BY_EMAIL;
import static com.aston.hw2.dao.util.RequestUtil.FIND_USER_BY_ID;

public class UserDao {

    public boolean addUser(User user) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_USER)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            int count = statement.executeUpdate();
            if (count == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<User> findByEmail(String userEmail) {
        Optional<User> user = Optional.empty();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_EMAIL)) {
            statement.setString(1, userEmail);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                user = Optional.of(User.builder().id(id).name(name).email(email).password(password).build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Optional<User> findById(long initId) {
        Optional<User> user = Optional.empty();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_ID)) {
            statement.setLong(1, initId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                user = Optional.of(User.builder().id(id).name(name).email(email).password(password).build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
