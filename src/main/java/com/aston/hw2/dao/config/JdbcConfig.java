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
        initDataBase();
    }

    private static void initDataBase() {
        Connection initConnection = null;
        PreparedStatement dbStatement = null;
        PreparedStatement indexStatement = null;
        try {
            initConnection = poolConnection.getConnection();

            dbStatement = initConnection
                    .prepareStatement(initSql("src/main/resources/schema.sql"));
            dbStatement.execute();

            indexStatement = initConnection
                    .prepareStatement(initSql("src/main/resources/index.sql"));
            indexStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dbStatement != null) dbStatement.close();
                if (indexStatement != null) indexStatement.close();
                if (initConnection != null) initConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String initSql(String path) {
        try (FileInputStream inputStream = new FileInputStream(path);
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

    public static Connection getConnection() throws SQLException {
        return poolConnection.getConnection();
    }
}
