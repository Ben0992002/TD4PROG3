package org.example;

import org.example.repository.K2Repository;

public class Main {
    public static void main(String[] args) {
        K2Repository repo = new K2Repository();

        System.out.println("--- RÉPONSES K2 ---");
        // a)
        repo.findVentesParMarque().forEach(v -> System.out.println("Marque: " + v.marque() + " | Ventes: " + v.nbrePiece()));

        // b)
        var p = repo.findVentesPivot();
        System.out.println("Pivot: GETZ=" + p.nbreGetz() + ", PRIDE=" + p.nbrePride() + ", LACETTI=" + p.nbreLacetti());

        // c)
        System.out.println("Total KIA: " + repo.findTotalPrixKia() + " Ar");
    }
}