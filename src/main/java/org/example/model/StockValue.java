package org.example.model;

/**
 * Représente une valeur de stock avec sa quantité et son unité
 */
public class StockValue {
    private Double quantity;
    private Unit unit;

    // Constructeurs
    public StockValue() {
    }

    public StockValue(Double quantity, Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    // Getters et Setters
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", quantity, unit);
    }
}
