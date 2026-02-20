package org.example.model;

/**
 * Record pour transporter les statistiques calculées par PostgreSQL.
 */
public record DishStats(
        String dishName,
        double totalCalories,
        double avgCaloriesPerIngredient,
        int ingredientCount
) {}
