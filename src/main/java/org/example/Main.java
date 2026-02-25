package org.example;

import org.example.model.CandidateVoteCount;
import org.example.model.VoteTypeCount;
import org.example.repository.DataRetriever;

import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Configuration de la connexion
        String url = "jdbc:postgresql://localhost:5432/election_db";
        String user = "election_user";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion réussie à la base de données !");

            DataRetriever retriever = new DataRetriever(conn);

            // --- Q1 & Q2 : Statistiques Globales (VALID, BLANK, NULL) ---
            System.out.println("\n--- [Q1 & Q2] Statistiques Globales ---");
            List<VoteTypeCount> stats = retriever.fetchVoteStats();
            if (stats.isEmpty()) {
                System.out.println("Aucune donnée de vote trouvée.");
            } else {
                stats.forEach(s -> System.out.println("Type: " + s.type() + " | Quantité: " + s.count()));
            }

            // --- Q3 & Q4 : Résultats détaillés et Pourcentages ---
            // Le calcul du % est fait directement par le SQL dans DataRetriever
            System.out.println("\n--- [Q3 & Q4] Scores des Candidats ---");
            List<CandidateVoteCount> results = retriever.fetchResults();

            results.forEach(r -> System.out.printf("Candidat: %-10s | Voix: %d | Pourcentage: %.2f%%\n",
                    r.name(), r.votes(), r.percentage()));

            // --- Q5 & Q6 : Proclamation du vainqueur ---
            System.out.println("\n--- [Q5 & Q6] Analyse des résultats ---");
            if (!results.isEmpty() && results.get(0).votes() > 0) {
                CandidateVoteCount winner = results.get(0);
                System.out.println("FÉLICITATIONS : Le vainqueur est " + winner.name() + " !");
            } else {
                System.out.println("ERREUR : Impossible de déterminer un vainqueur (aucun vote valide).");
            }

        } catch (SQLException e) {
            System.err.println("Erreur de connexion ou de requête : " + e.getMessage());
            e.printStackTrace();
        }
    }
}