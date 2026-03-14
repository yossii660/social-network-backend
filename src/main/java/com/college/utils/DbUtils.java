package com.college.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class DbUtils {
    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            String host = "localhost";
            String username = "root";
            String password = "1234";
            String schema = "social_network";
            int port = 3306;
            String url = "jdbc:mysql://"
                    + host + ":" + port + "/"
                    + schema;
            this.connection =
                    DriverManager.getConnection(url, username, password);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Failed to create db connection");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
