package com.aston.hw2.dao.util;

public class RequestUtil {
    public static final String ADD_USER = "INSERT INTO users (" +
                                          "name, " +
                                          "email, " +
                                          "password) " +
                                          "VALUES (?, ?, ?)";
    public static final String FIND_USER_BY_EMAIL = "SELECT * " +
                                                    "FROM users AS us " +
                                                    "WHERE us.email = ?";
    public static final String FIND_USER_BY_ID = "SELECT * " +
                                                 "FROM users AS us " +
                                                 "WHERE us.id = ?";
    public static final String ADD_EVENT = "INSERT INTO events (" +
                                           "title, " +
                                           "description, " +
                                           "event_date, " +
                                           "initiator_id, " +
                                           "location_id) " +
                                           "VALUES (?, ?, ?, ?, ?)";
    public static final String FIND_EVENT_BY_ID = "SELECT * " +
                                                  "FROM events AS ev " +
                                                  "WHERE ev.id = ?";
    public static final String FIND_EVENT_BY_ID_AND_INITIATOR = "SELECT * " +
                                                                "FROM events AS ev " +
                                                                "WHERE ev.id = ? " +
                                                                "AND ev.initiator_id = ?";
    public static final String UPDATE_EVENT = "UPDATE events " +
                                              "SET " +
                                              "title = ?, " +
                                              "description = ?, " +
                                              "event_date = ? " +
                                              "WHERE id = ?";
    public static final String GET_ALL_EVENTS = "SELECT * " +
                                                "FROM events";
    public static final String DELETE_EVENT_BY_ID = "DELETE " +
                                                    "FROM events " +
                                                    "WHERE id = ?";
    public static final String ADD_LOCATION = "INSERT INTO locations (" +
                                              "radius, " +
                                              "lat, " +
                                              "lon) " +
                                              "VALUES (?, ?, ?)";
    public static final String FIND_LOCATION_BY_ID = "SELECT * " +
                                                     "FROM locations AS l " +
                                                     "WHERE l.id = ?";
    public static final String UPDATE_LOCATION = "UPDATE locations " +
                                                 "SET " +
                                                 "lat = ?, " +
                                                 "lon = ?, " +
                                                 "radius = ? " +
                                                 "WHERE id = ?";
    public static final String DELETE_LOC_BY_ID = "DELETE " +
                                                  "FROM locations " +
                                                  "WHERE id = ?";
}
