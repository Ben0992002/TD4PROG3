package org.example;


import org.example.repository.GarageRetriever;
import org.example.model.StatsReparation;

public class Main {
    public static void main(String[] args) {
        GarageRetriever retriever = new GarageRetriever();

        System.out.println("--- Question (a) ---");
        for (StatsReparation s : retriever.findNombreReparationsParModele()) {
            System.out.println(s.marque() + " " + s.modele() + " : " + s.nombreReparations());
        }

        System.out.println("\n--- Question (b) ---");
        retriever.displayPourcentagesParModele();

        System.out.println("\n--- Question (c) ---");
        for (StatsReparation s : retriever.findCoutParMarqueEtMecanicien()) {
            System.out.println(s.marque() + " | " + s.nomMecanicien() + " : " + s.montantTotal() + " Ar");
        }

        System.out.println("\n--- Question (d) ---");
        StatsReparation moins = retriever.findMoinsRentable();
        if (moins != null) {
            System.out.println("Nom : " + moins.nomMecanicien() + " | Total : " + moins.montantTotal());
        }
    }
}