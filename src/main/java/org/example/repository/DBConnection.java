package org.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public Connection getConnection() {
        try {
            String jdbcUrl = System.getenv("JDBC_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (jdbcUrl == null) jdbcUrl = "jdbc:postgresql://localhost:5432/mini_dish_db";
            if (user == null) user = "mini_dish_db_manager";
            if (password == null) password = "123456";

            return DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion : Vérifiez vos variables d'environnement.", e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}