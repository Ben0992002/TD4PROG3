package org.example.repository;

import org.example.model.*;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
/**
 * Repository unifié pour les ingrédients, stocks et statistiques (TD4 & TD5)
 */
public class DataRetriever {

    // --- MÉTHODES TD4 : CRUD & MOUVEMENTS ---

    public Ingredient findIngredientById(Integer id) {
        // Utilisation du try-with-resources pour garantir la fermeture de la connexion
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT id, name, price, category FROM ingredient WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                ingredient.setPrice(rs.getDouble("price"));
                ingredient.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ingredient.setStockMovementList(findStockMovementsByIngredientId(id));
                return ingredient;
            }
            throw new RuntimeException("Ingredient not found with ID: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche", e);
        }
    }

    private List<StockMovement> findStockMovementsByIngredientId(Integer ingredientId) {
        List<StockMovement> movements = new ArrayList<>();
        try (Connection connection = new DBConnection().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT id, quantity, unit, type, creation_datetime FROM stock_movement WHERE id_ingredient = ? ORDER BY creation_datetime ASC");
            ps.setInt(1, ingredientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                StockMovement movement = new StockMovement();
                movement.setId(rs.getInt("id"));
                movement.setType(MovementTypeEnum.valueOf(rs.getString("type")));
                movement.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());

                StockValue value = new StockValue();
                value.setQuantity(rs.getDouble("quantity"));
                value.setUnit(Unit.valueOf(rs.getString("unit")));

                movement.setValue(value);
                movements.add(movement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movements;
    }

    public Ingredient saveIngredient(Ingredient toSave) throws SQLException {
        try (Connection connection = new DBConnection().getConnection()) {
            connection.setAutoCommit(false);
            try {
                Integer id = (toSave.getId() == null) ? insertIngredient(connection, toSave) : updateIngredient(connection, toSave);
                if (toSave.getStockMovementList() != null) {
                    saveStockMovements(connection, id, toSave.getStockMovementList());
                }
                connection.commit();
                return findIngredientById(id);
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // --- MÉTHODES TD5 : PUSH-DOWN PROCESSING & STATS ---

    /**
     * Q1 : Calcul du stock en temps réel par SQL
     */
    public StockValue getStockValueAt(Instant t, Integer ingredientId) {
        String query = """
            SELECT unit, SUM(CASE WHEN type = 'OUT' THEN -quantity ELSE quantity END) as actual_stock
            FROM stock_movement WHERE id_ingredient = ? AND creation_datetime <= ? GROUP BY unit
        """;
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(t));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockValue sv = new StockValue();
                    sv.setQuantity(rs.getDouble("actual_stock"));
                    sv.setUnit(Unit.valueOf(rs.getString("unit")));
                    return sv;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur Push-down Q1", e);
        }
        return null;
    }

    /**
     * Q2 : Coût de revient financier par plat
     */
    public List<DishCost> computeDishCosts() {
        List<DishCost> costs = new ArrayList<>();
        String sql = """
            SELECT p.name as dish_name, SUM(c.quantity * i.price) as total_cost
            FROM dish p
            JOIN composition c ON p.id = c.dish_id
            JOIN ingredient i ON c.ingredient_id = i.id
            GROUP BY p.name;
        """;
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                costs.add(new DishCost(rs.getString("dish_name"), rs.getBigDecimal("total_cost")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur Q2 : " + e.getMessage());
        }
        return costs;
    }

    /**
     * Q3 : Statistiques nutritionnelles par plat
     */
    public List<DishStats> computeDishStatistics() {
        List<DishStats> stats = new ArrayList<>();
        String sql = """
            SELECT p.name as dish_name, 
                   SUM(c.quantity * i.calories_per_unit) as total_calories,
                   AVG(i.calories_per_unit) as avg_calories_per_ingredient,
                   COUNT(c.ingredient_id) as ingredient_count
            FROM dish p
            JOIN composition c ON p.id = c.dish_id
            JOIN ingredient i ON c.ingredient_id = i.id
            GROUP BY p.name;
        """;
        try (Connection conn = new DBConnection().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.add(new DishStats(
                        rs.getString("dish_name"),
                        rs.getDouble("total_calories"),
                        rs.getDouble("avg_calories_per_ingredient"),
                        rs.getInt("ingredient_count")));
            }
        } catch (SQLException e) {
            System.err.println("Erreur Q3 : " + e.getMessage());
        }
        return stats;
    }

    // --- MÉTHODES PRIVÉES UTILITAIRES ---

    private Integer insertIngredient(Connection conn, Ingredient ing) throws SQLException {
        String sql = "INSERT INTO ingredient (id, name, category, price) VALUES (?, ?, ?::ingredient_category, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (ing.getId() != null) ? ing.getId() : getNextSerialValue(conn, "ingredient", "id"));
            ps.setString(2, ing.getName());
            ps.setString(3, ing.getCategory().name());
            ps.setDouble(4, ing.getPrice());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    private Integer updateIngredient(Connection conn, Ingredient ing) throws SQLException {
        String sql = "UPDATE ingredient SET name = ?, category = ?::ingredient_category, price = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ing.getName());
            ps.setString(2, ing.getCategory().name());
            ps.setDouble(3, ing.getPrice());
            ps.setInt(4, ing.getId());
            ps.executeUpdate();
            return ing.getId();
        }
    }

    private void saveStockMovements(Connection conn, Integer ingId, List<StockMovement> movements) throws SQLException {
        String sql = "INSERT INTO stock_movement (id, id_ingredient, quantity, unit, type, creation_datetime) VALUES (?, ?, ?, ?::unit, ?::movement_type, ?) ON CONFLICT (id) DO NOTHING";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (StockMovement m : movements) {
                ps.setInt(1, (m.getId() != null) ? m.getId() : getNextSerialValue(conn, "stock_movement", "id"));
                ps.setInt(2, ingId);
                ps.setDouble(3, m.getValue().getQuantity());
                ps.setString(4, m.getValue().getUnit().name());
                ps.setString(5, m.getType().name());
                ps.setTimestamp(6, Timestamp.from(m.getCreationDatetime()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private int getNextSerialValue(Connection conn, String table, String col) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(String.format("SELECT COALESCE(MAX(%s), 0) + 1 FROM %s", col, table))) {
            rs.next();
            return rs.getInt(1);
        }
    }
}