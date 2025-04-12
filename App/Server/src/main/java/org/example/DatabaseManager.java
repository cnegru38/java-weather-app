package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:users.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                // Baza de date este creată dacă nu există
                System.out.println("Database created successfully.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
