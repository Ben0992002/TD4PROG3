package org.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/hei_prog3_td5";
    private static final String USER = "hei_user";
    private static final String PASSWORD = "hei_password";

    private static Connection connection;

    /**
     * Méthode pour obtenir l'instance unique de la connexion.
     */
    public static Connection getConnection() {
        try {
            // Si la connexion n'existe pas ou est fermée, on la crée
            if (connection == null || connection.isClosed()) {
                // Chargement explicite du driver (optionnel mais rigoureux)
                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connexion à PostgreSQL établie avec succès.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver PostgreSQL introuvable ! Ajoutez la dépendance Maven.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Fermeture proprement de la connexion à la fin du programme.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔌 Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
