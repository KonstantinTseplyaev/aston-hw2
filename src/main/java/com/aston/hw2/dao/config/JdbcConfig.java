package com.aston.hw2.dao.config;


import org.postgresql.ds.PGConnectionPoolDataSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConfig {
    private final static String URL;
    private final static String USERNAME;
    private final static String PASSWORD;
    private final static PGConnectionPoolDataSource poolConnection;

    static {
        FileInputStream inputStream;
        Properties properties = new Properties();

        try {
            inputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException exp) {
            exp.printStackTrace();
        }

        URL = properties.getProperty("db.url");
        USERNAME = properties.getProperty("db.username");
        PASSWORD = properties.getProperty("db.password");

        poolConnection = new PGConnectionPoolDataSource();
        poolConnection.setURL(URL);
        poolConnection.setUser(USERNAME);
        poolConnection.setPassword(PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = poolConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(initSql());
        statement.execute();
        return connection;
    }


    private static String initSql() {
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
