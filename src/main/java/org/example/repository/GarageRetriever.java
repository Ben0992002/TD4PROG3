package org.example.repository;

import org.example.connection.DatabaseConnection;
import org.example.model.StatsReparation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GarageRetriever {

    // --- QUESTION (a) : Nombre de réparations par marque et modèle ---
    public List<StatsReparation> findNombreReparationsParModele() {
        List<StatsReparation> stats = new ArrayList<>();
        String query = "SELECT mv.marque, mv.modele, COUNT(r.id) as nbre " +
                "FROM modele_voiture mv " +
                "LEFT JOIN reparation r ON mv.id = r.id_modele_voiture " +
                "GROUP BY mv.marque, mv.modele";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                StatsReparation s = new StatsReparation();
                s.setMarque(rs.getString("marque"));
                s.setModele(rs.getString("modele"));
                s.setNombreReparations(rs.getInt("nbre"));
                stats.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    // --- QUESTION (b) : Pourcentages en UNE SEULE LIGNE ---
    public void displayPourcentagesParModele() {
        String query = "SELECT " +
                "ROUND(100.0 * SUM(CASE WHEN mv.modele = 'RANGER' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) AS p_ranger, " +
                "ROUND(100.0 * SUM(CASE WHEN mv.modele = 'EVEREST' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) AS p_everest, " +
                "ROUND(100.0 * SUM(CASE WHEN mv.modele = 'YUKON' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) AS p_yukon, " +
                "ROUND(100.0 * SUM(CASE WHEN mv.modele = 'RAM' THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0), 2) AS p_ram " +
                "FROM reparation r JOIN modele_voiture mv ON r.id_modele_voiture = mv.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Ranger: " + rs.getDouble("p_ranger") + "% | " +
                        "Everest: " + rs.getDouble("p_everest") + "% | " +
                        "Yukon: " + rs.getDouble("p_yukon") + "% | " +
                        "Ram: " + rs.getDouble("p_ram") + "%");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- QUESTION (c) : LE COUT PAR MARQUE ET PAR MECANICIEN ---
    // C'est la méthode que j'avais omise !
    public List<StatsReparation> findCoutParMarqueEtMecanicien() {
        List<StatsReparation> stats = new ArrayList<>();
        // Jointure triple : Réparation + Mécanicien + Modèle pour avoir le nom et la marque
        String query = "SELECT mv.marque, m.nom, SUM(r.cout) as total_cout " +
                "FROM reparation r " +
                "JOIN mecanicien m ON r.id_mecanicien = m.id " +
                "JOIN modele_voiture mv ON r.id_modele_voiture = mv.id " +
                "GROUP BY mv.marque, m.nom " +
                "ORDER BY mv.marque, total_cout DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                StatsReparation s = new StatsReparation();
                s.setMarque(rs.getString("marque"));
                s.setNomMecanicien(rs.getString("nom"));
                s.setMontantTotal(rs.getDouble("total_cout"));
                stats.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return stats;
    }

    // --- QUESTION (d) : Le mécanicien le moins rentable ---
    public StatsReparation findMoinsRentable() {
        String query = "SELECT m.nom, SUM(r.cout) as total " +
                "FROM mecanicien m " +
                "JOIN reparation r ON m.id = r.id_mecanicien " +
                "GROUP BY m.nom " +
                "ORDER BY total ASC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                StatsReparation s = new StatsReparation();
                s.setNomMecanicien(rs.getString("nom"));
                s.setMontantTotal(rs.getDouble("total"));
                return s;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}