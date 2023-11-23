package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {


    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/selutable";
        private static final String dbUser = "selu";
        private static final String dbPassword = "123654selu";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
        }
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Make sure you're using the "selutable" database
            conn.createStatement().executeUpdate("USE selutable");

            String createTableQuery = "CREATE TABLE IF NOT EXISTS items ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255) NOT NULL,"
                    + "price DOUBLE PRECISION NOT NULL,"
                    + "stockQuantity INT NOT NULL"
                    + ")";

            conn.createStatement().executeUpdate(createTableQuery);

            System.out.println("Database schema initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
}
}


