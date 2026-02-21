package org.example.model;

public record OrderSuggestion(
    Integer ingredientId,
    String ingredientName,
    double requiredQuantity
) {}
