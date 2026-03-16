package org.example.repository;

import org.example.DatabaseConnection;
import org.example.model.VenteParMarque;
import org.example.model.VentePivot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class K2Repository {

    /**
     * Calcule le chiffre d'affaires total par marque.
     * Utilise le Push Down Processing via une jointure SQL.
     */
    public List<VenteParMarque> getChiffreAffairesParMarque() {
        List<VenteParMarque> resultats = new ArrayList<>();

        // La requête SQL effectue la jointure et l'agrégation (SUM et GROUP BY)
        String sql = """
            SELECT v.marque, SUM(ve.quantite * v.prix_unitaire) as chiffre_affaires
            FROM voiture v
            JOIN vente ve ON v.id_voiture = ve.id_voiture
            GROUP BY v.marque
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                resultats.add(new VenteParMarque(
                        rs.getString("marque"),
                        rs.getDouble("chiffre_affaires")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des ventes : " + e.getMessage());
        }
        return resultats;
    }

    public List<VentePivot> getVentesPivot() {
        List<VentePivot> resultats = new ArrayList<>();

        // Requête "Pivot" : on somme les quantités selon le mois
        String sql = """
        SELECT 
            v.marque,
            SUM(CASE WHEN EXTRACT(MONTH FROM ve.date_vente) = 1 THEN ve.quantite ELSE 0 END) as jan,
            SUM(CASE WHEN EXTRACT(MONTH FROM ve.date_vente) = 2 THEN ve.quantite ELSE 0 END) as feb,
            SUM(CASE WHEN EXTRACT(MONTH FROM ve.date_vente) = 3 THEN ve.quantite ELSE 0 END) as mar
        FROM voiture v
        JOIN vente ve ON v.id_voiture = ve.id_voiture
        GROUP BY v.marque
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                resultats.add(new VentePivot(
                        rs.getString("marque"),
                        rs.getInt("jan"),
                        rs.getInt("feb"),
                        rs.getInt("mar")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultats;
    }
}