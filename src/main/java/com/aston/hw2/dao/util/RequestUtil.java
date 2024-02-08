package com.aston.hw2.dao.util;

public class RequestUtil {
    public static final String ADD_USER = "insert into " +
            "users (name, email, password) " +
            "values (?, ?, ?)";
    public static final String FIND_USER_BY_EMAIL = "select * " +
            "from users as us " +
            "where us.email = ?";
    public static final String FIND_USER_BY_ID = "select * " +
            "from users as us " +
            "where us.id = ?";
    public static final String ADD_EVENT = "insert into " +
            "events (title, description, event_date, initiator_id, location_id) " +
            "values (?, ?, ?, ?, ?)";
    public static final String FIND_EVENT_BY_ID = "select * " +
            "from events as ev " +
            "where ev.id = ?";
    public static final String FIND_EVENT_BY_ID_AND_INITIATOR = "select * " +
            "from events as ev " +
            "where ev.id = ? " +
            "and ev.initiator_id = ?";
    public static final String UPDATE_EVENT = "update " +
            "events set title = ?, description = ?, event_date = ? " +
            "where id = ?";
    public static final String GET_ALL_EVENTS = "select * " +
            "from events";
    public static final String DELETE_EVENT_BY_ID = "delete from events " +
            "where id = ?";
    public static final String ADD_LOCATION = "insert into " +
            "locations (radius, lat, lon) " +
            "values (?, ?, ?)";
    public static final String FIND_LOCATION_BY_ID = "select * " +
            "from locations as l " +
            "where l.id = ?";
    public static final String UPDATE_LOCATION = "update " +
            "locations set lat = ?, lon = ?, radius = ? " +
            "where id = ?";
    public static final String DELETE_LOC_BY_ID = "delete from locations " +
            "where id = ?";
}
