package org.example.model;

/**
 * Modèle représentant une facture telle qu'elle est stockée en base.
 */
public class Invoice {
    private int id;
    private String customerName;
    private String status; // On peut utiliser un String ou une Enum ici

    // Constructeur par défaut
    public Invoice() {}

    // Constructeur complet
    public Invoice(int id, String customerName, String status) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
    }

    // Getters et Setters (indispensables pour les outils de mapping)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + status;
    }
}
