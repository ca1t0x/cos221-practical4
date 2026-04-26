package com.mycompany.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/chinook"; // change later if needed
            String user = "root"; // change if needed
            String password = "password"; // change if needed

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database!");
            return conn;

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}