package com.aston.hw2.dao;


import com.aston.hw2.dao.config.JdbcConfig;
import com.aston.hw2.model.Event;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.aston.hw2.dao.util.RequestUtil.ADD_EVENT;
import static com.aston.hw2.dao.util.RequestUtil.DELETE_EVENT_BY_ID;
import static com.aston.hw2.dao.util.RequestUtil.FIND_EVENT_BY_ID;
import static com.aston.hw2.dao.util.RequestUtil.FIND_EVENT_BY_ID_AND_INITIATOR;
import static com.aston.hw2.dao.util.RequestUtil.GET_ALL_EVENTS;
import static com.aston.hw2.dao.util.RequestUtil.UPDATE_EVENT;


@Slf4j
public class EventDao {

    public boolean add(Event event) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_EVENT)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(event.getEventDate()));
            statement.setLong(4, event.getInitiatorId());
            statement.setLong(5, event.getLocationId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_EVENTS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = Event.builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .eventDate((resultSet.getTimestamp("event_date")).toLocalDateTime())
                        .initiatorId(resultSet.getLong("initiator_id"))
                        .locationId(resultSet.getLong("location_id"))
                        .build();
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public Optional<Event> findById(long id) {
        Optional<Event> eventOpt = Optional.empty();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_EVENT_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = Event.builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .eventDate((resultSet.getTimestamp("event_date")).toLocalDateTime())
                        .initiatorId(resultSet.getLong("initiator_id"))
                        .locationId(resultSet.getLong("location_id"))
                        .build();
                eventOpt = Optional.of(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventOpt;
    }

    public Optional<Event> findByIdAndInitiator(Long id, Long initId) {
        Optional<Event> eventOpt = Optional.empty();
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_EVENT_BY_ID_AND_INITIATOR)) {
            statement.setLong(1, id);
            statement.setLong(2, initId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = Event.builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .description(resultSet.getString("description"))
                        .eventDate((resultSet.getTimestamp("event_date")).toLocalDateTime())
                        .initiatorId(resultSet.getLong("initiator_id"))
                        .locationId(resultSet.getLong("location_id"))
                        .build();
                eventOpt = Optional.of(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventOpt;
    }

    public boolean update(Event event) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EVENT)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(event.getEventDate()));
            statement.setLong(4, event.getId());
            int count = statement.executeUpdate();
            if (count == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteById(long eventId) {
        try (Connection connection = JdbcConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_EVENT_BY_ID)) {
            statement.setLong(1, eventId);
            int count = statement.executeUpdate();
            if (count == 1) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
