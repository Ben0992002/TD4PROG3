package org.example.model;

/**
 * Représente le résultat d'une agrégation SQL (Push Down Processing).
 * @param marque Nom de la marque automobile.
 * @param chiffreAffaires Montant total généré (calculé par la DB).
 */
public record VenteParMarque(String marque, double chiffreAffaires) {}