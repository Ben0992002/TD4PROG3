package org.example.model;

public class StatsReparation {
    private String marque;
    private String modele;
    private int nombreReparations;
    private String nomMecanicien;
    private double montantTotal;

    // Ajout des méthodes sans "get"
    public String marque() { return marque; }
    public String modele() { return modele; }
    public int nombreReparations() { return nombreReparations; }
    public String nomMecanicien() { return nomMecanicien; }
    public double montantTotal() { return montantTotal; }

    // setters
    public void setMarque(String marque) { this.marque = marque; }
    public void setModele(String modele) { this.modele = modele; }
    public void setNombreReparations(int nbre) { this.nombreReparations = nbre; }
    public void setNomMecanicien(String nom) { this.nomMecanicien = nom; }
    public void setMontantTotal(double montant) { this.montantTotal = montant; }

    @Override
    public String toString() {
        return "StatsReparation{" +
                "marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", nbre=" + nombreReparations +
                ", nom='" + nomMecanicien + '\'' +
                ", total=" + montantTotal +
                '}';
    }
}