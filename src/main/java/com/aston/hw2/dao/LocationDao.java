package com.aston.hw2.dao;

import com.aston.hw2.dao.config.JdbcConfig;
import com.aston.hw2.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

import static com.aston.hw2.dao.util.RequestUtil.ADD_LOCATION;
import static com.aston.hw2.dao.util.RequestUtil.DELETE_LOC_BY_ID;
import static com.aston.hw2.dao.util.RequestUtil.FIND_LOCATION_BY_ID;
import static com.aston.hw2.dao.util.RequestUtil.UPDATE_LOCATION;

public class LocationDao {

    public long addLocation(Location location) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_LOCATION, new String[]{"id"})) {
            if (location.getRadius() != null) {
                statement.setDouble(1, location.getRadius());
            } else {
                statement.setNull(1, Types.DOUBLE);
            }
            statement.setDouble(2, location.getLat());
            statement.setDouble(3, location.getLon());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Optional<Location> findById(long locId) {
        Optional<Location> location = Optional.empty();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_LOCATION_BY_ID)) {
            statement.setLong(1, locId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Double lat = resultSet.getDouble("lat");
                Double lon = resultSet.getDouble("lon");
                Double radius = resultSet.getDouble("radius");
                location = Optional.of(Location.builder().id(id).lat(lat).lon(lon).radius(radius).build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }

    public boolean updateLocation(Location targetLoc) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LOCATION)) {
            statement.setDouble(1, targetLoc.getLat());
            statement.setDouble(2, targetLoc.getLon());
            statement.setDouble(3, targetLoc.getRadius());
            statement.setLong(4, targetLoc.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(long locId) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_LOC_BY_ID)) {
            statement.setLong(1, locId);
            int count = statement.executeUpdate();
            if (count == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
