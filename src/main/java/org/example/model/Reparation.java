package org.example.model;

/**
 * Représente une intervention (id, id_mecanicien, id_modele_voiture, cout).
 */
public record Reparation(
        int id,
        int idMecanicien,
        int idModeleVoiture,
        double cout
) {}
