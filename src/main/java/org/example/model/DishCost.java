package org.example.model;

import java.math.BigDecimal;

public record DishCost(
        String dishName,
        BigDecimal totalCost
) {}
