// Version finale Examen K2 - 2026
package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/examen_k2";
    private static final String USER = "k2_admin";
    private static final String PASS = "k2_password";

    public static Connection getConnection() throws SQLException {
        try {
            // Optionnel mais c'est recommandé pour charger le driver explicitement.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Pilote PostgreSQL introuvable", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
