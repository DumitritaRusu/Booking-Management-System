package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** PostgreSQL connection configured using environment variables (no hardcoded passwords). */
public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/proiect_booking";

    private static String envOrDefault(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {
        String url = envOrDefault("DB_URL", DEFAULT_URL);
        String user = envOrDefault("DB_USER", "postgres");
        String password = System.getenv("DB_PASSWORD");
        if (password == null) {
            throw new IllegalStateException(
                "Set the DB_PASSWORD environment variable before running the application."
            );
        }
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Connected to PostgreSQL successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
