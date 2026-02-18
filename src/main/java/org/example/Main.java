package org.example;

import org.example.repository.DatabaseConnection;
import org.example.repository.InvoiceRepository;
import org.example.model.*;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Initialisation du pont (Connexion)
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.err.println("Impossible de démarrer l'application sans base de données.");
            return;
        }

        // 2. Initialisation du Repository
        InvoiceRepository repository = new InvoiceRepository(connection);

        System.out.println("=== RÉSULTATS DU TD : PUSH-DOWN PROCESSING ===\n");

        // --- Q1 : Total par facture ---
        System.out.println("Q1 - Total par facture :");
        repository.findInvoiceTotals().forEach(t ->
                System.out.printf("%d | %s | %.2f%n", t.id(), t.customerName(), t.totalAmount()));

        // --- Q2 : Confirmées et Payées ---
        System.out.println("\nQ2 - Total factures CONFIRMED ou PAID :");
        repository.findConfirmedAndPaidInvoiceTotals().forEach(t ->
                System.out.printf("%d | %s | %s | %.2f%n", t.id(), t.customerName(), t.status(), t.totalAmount()));

        // --- Q3 : Totaux cumulés par statut ---
        System.out.println("\nQ3 - Totaux cumulés par statut :");
        InvoiceStatusTotals statusTotals = repository.computeStatusTotals();
        if (statusTotals != null) {
            System.out.println("total_paid = " + statusTotals.totalPaid());
            System.out.println("total_confirmed = " + statusTotals.totalConfirmed());
            System.out.println("total_draft = " + statusTotals.totalDraft());
        }

        // --- Q4 : CA Pondéré ---
        System.out.println("\nQ4 - Chiffre d'affaires pondéré :");
        System.out.printf("CA Pondéré : %.2f%n", repository.computeWeightedTurnover());

        // --- Q5-A : Détails TVA ---
        System.out.println("\nQ5-A - Totaux HT, TVA et TTC :");
        repository.findInvoiceTaxSummaries().forEach(s ->
                System.out.printf("%d | HT %.2f | TVA %.2f | TTC %.2f%n",
                        s.id(), s.totalHT(), s.totalTVA(), s.totalTTC()));

        // --- Q5-B : CA TTC Pondéré ---
        System.out.println("\nQ5-B - Chiffre d'affaires TTC pondéré :");
        System.out.println("Total TTC Pondéré : " + repository.computeWeightedTurnoverTtc());

        // 3. Fermeture propre de la connexion
        DatabaseConnection.closeConnection();
    }
}