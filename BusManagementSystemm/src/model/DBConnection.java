package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // FIX: Changed 'demo_db' to 'Demo_db' — must match your MySQL database name exactly
    private static final String URL = "jdbc:mysql://localhost:3306/Demo_db";
    private static final String USER = "root";
    private static final String PASSWORD = "mysqlmerka";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected.");
            return conn;

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}