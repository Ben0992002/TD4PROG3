package org.example.model;

/**
 * Représente un modèle de voiture selon l'énoncé (id, marque, modele).
 */
public record ModeleVoiture(
        int id,
        String marque,
        String modele
) {}
