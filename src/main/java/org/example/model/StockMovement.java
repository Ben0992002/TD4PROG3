package org.example.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Représente un mouvement de stock (entrée ou sortie)
 */
public class StockMovement {
    private Integer id;
    private MovementTypeEnum type;
    private Instant creationDatetime;
    private StockValue value;

    // Constructeurs
    public StockMovement() {
    }

    public StockMovement(Integer id, MovementTypeEnum type, Instant creationDatetime, StockValue value) {
        this.id = id;
        this.type = type;
        this.creationDatetime = creationDatetime;
        this.value = value;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public void setType(MovementTypeEnum type) {
        this.type = type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StockMovement that)) return false;
        return Objects.equals(id, that.id) && 
               type == that.type && 
               Objects.equals(creationDatetime, that.creationDatetime) && 
               Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, creationDatetime, value);
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", type=" + type +
                ", creationDatetime=" + creationDatetime +
                ", value=" + value +
                '}';
    }
}
