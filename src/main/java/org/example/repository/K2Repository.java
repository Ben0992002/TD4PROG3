package org.example.repository;

import org.example.DatabaseConnection;
import org.example.model.VenteParMarque;
import org.example.model.VentePivot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class K2Repository {
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    // a) Nombre de pièces par marque
    public List<VenteParMarque> findVentesParMarque() {
        List<VenteParMarque> list = new ArrayList<>();
        String sql = "SELECT mv.marque, SUM(v.quantite) as nbre " +
                "FROM Vente v " +
                "JOIN Piece_auto pa ON v.id_piece_auto = pa.id " +
                "JOIN Modele_voiture mv ON pa.id_modele_voiture = mv.id " +
                "GROUP BY mv.marque";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new VenteParMarque(rs.getString("marque"), rs.getInt("nbre")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    // b) Nombre de pièces par modèle (Pivot une seule ligne)
    public VentePivot findVentesPivot() {
        String sql = "SELECT " +
                "SUM(CASE WHEN mv.modele = 'GETZ' THEN v.quantite ELSE 0 END) as getz, " +
                "SUM(CASE WHEN mv.modele = 'PRIDE' THEN v.quantite ELSE 0 END) as pride, " +
                "SUM(CASE WHEN mv.modele = 'LACETTI' THEN v.quantite ELSE 0 END) as lacetti " +
                "FROM Vente v " +
                "JOIN Piece_auto pa ON v.id_piece_auto = pa.id " +
                "JOIN Modele_voiture mv ON pa.id_modele_voiture = mv.id";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new VentePivot(rs.getInt("getz"), rs.getInt("pride"), rs.getInt("lacetti"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    // c) Prix total pour KIA
    public double findTotalPrixKia() {
        String sql = "SELECT SUM(pa.prix * v.quantite) as total " +
                "FROM Vente v " +
                "JOIN Piece_auto pa ON v.id_piece_auto = pa.id " +
                "JOIN Modele_voiture mv ON pa.id_modele_voiture = mv.id " +
                "WHERE mv.marque = 'KIA'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) { throw new RuntimeException(e); }
        return 0;
    }
}