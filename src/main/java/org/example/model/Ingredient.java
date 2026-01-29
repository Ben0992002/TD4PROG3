package org.example.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static java.time.Instant.now;

/**
 * Représente un ingrédient avec ses mouvements de stock
 */
public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
    private List<StockMovement> stockMovementList;

    // Constructeurs
    public Ingredient() {
    }

    public Ingredient(Integer id, String name, CategoryEnum category, Double price, 
                     List<StockMovement> stockMovementList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockMovementList = stockMovementList;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
    }

    /**
     * Calcule le stock disponible à un instant donné
     * 
     * Algorithme :
     * 1. Filtrer les mouvements avant ou égal à l'instant t
     * 2. Vérifier qu'il n'y a qu'une seule unité (pas de conversion gérée)
     * 3. Sommer les entrées (IN)
     * 4. Sommer les sorties (OUT)
     * 5. Stock = entrées - sorties
     * 
     * @param t l'instant pour lequel calculer le stock
     * @return le stock disponible avec son unité
     * @throws RuntimeException si plusieurs unités sont trouvées
     */
    public StockValue getStockValueAt(Instant t) {
        if (stockMovementList == null || stockMovementList.isEmpty()) {
            return null;
        }

        // Vérifier qu'il n'y a qu'une seule unité
        Map<Unit, List<StockMovement>> unitSet = stockMovementList.stream()
                .collect(Collectors.groupingBy(stockMovement -> stockMovement.getValue().getUnit()));

        if (unitSet.keySet().size() > 1) {
            throw new RuntimeException(
                "Multiple units found for ingredient '" + name + "' and conversion is not handled"
            );
        }

        // Filtrer les mouvements jusqu'à l'instant t
        List<StockMovement> stockMovements = stockMovementList.stream()
                .filter(stockMovement -> !stockMovement.getCreationDatetime().isAfter(t))
                .toList();

        // Calculer les entrées (IN)
        double movementIn = stockMovements.stream()
                .filter(stockMovement -> stockMovement.getType().equals(MovementTypeEnum.IN))
                .flatMapToDouble(stockMovement -> DoubleStream.of(stockMovement.getValue().getQuantity()))
                .sum();

        // Calculer les sorties (OUT)
        double movementOut = stockMovements.stream()
                .filter(stockMovement -> stockMovement.getType().equals(MovementTypeEnum.OUT))
                .flatMapToDouble(stockMovement -> DoubleStream.of(stockMovement.getValue().getQuantity()))
                .sum();

        // Créer le résultat
        StockValue stockValue = new StockValue();
        stockValue.setQuantity(movementIn - movementOut);
        stockValue.setUnit(unitSet.keySet().stream().findFirst().get());

        return stockValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(name, that.name) && 
               category == that.category && 
               Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", price=" + price +
                ", actualStock=" + getStockValueAt(now()) +
                '}';
    }
}
