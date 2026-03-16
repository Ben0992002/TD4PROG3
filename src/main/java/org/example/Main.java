// Version finale Examen K2 - 2026
package org.example;

import org.example.repository.K2Repository;

public class Main {
    public static void main(String[] args) {
        K2Repository repo = new K2Repository();

        // 1. Test du Chiffre d'Affaires par Marque (Jointure)
        System.out.println("=== CHIFFRE D'AFFAIRES PAR MARQUE (Push Down) ===");
        repo.getChiffreAffairesParMarque().forEach(v ->
                System.out.printf("Marque: %-10s | CA Total: %.2f €%n", v.marque(), v.chiffreAffaires())
        );

        System.out.println("\n");

        // 2. Test du Tableau Pivot (Ventes par mois)
        System.out.println("=== TABLEAU PIVOT DES VENTES (Quantités) ===");
        System.out.println("Marque     | Janv | Févr | Mars");
        System.out.println("-------------------------------");
        repo.getVentesPivot().forEach(p ->
                System.out.printf("%-10s | %4d | %4d | %4d%n",
                        p.marque(), p.janvier(), p.fevrier(), p.mars())
        );
    }
}
