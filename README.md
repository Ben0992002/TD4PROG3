# TD4 - Gestion des Stocks 📦

## 🎯 Objectif
Gérer les mouvements de stocks des ingrédients (entrées et sorties) et calculer le stock disponible à tout moment.

---

## 📁 Structure du projet

```
src/main/java/org/example/
│
├── model/
│   ├── Unit.java                    (Enum: PCS, KG, L)
│   ├── MovementTypeEnum.java        (Enum: IN, OUT)
│   ├── CategoryEnum.java            (Enum: VEGETABLE, ANIMAL, MARINE, DAIRY, OTHER)
│   ├── StockValue.java              (Quantité + Unité)
│   ├── StockMovement.java           (ID, Value, Type, Date)
│   └── Ingredient.java              (ID, Name, List<StockMovement> + getStockValueAt)
│
├── repository/
│   ├── DBConnection.java            (Gestionnaire de connexion PostgreSQL)
│   └── DataRetriever.java           (CRUD pour Ingredient + Stock)
│
└── Main.java                        (Test du calcul de stock de la Laitue)
```

---

## 🗄️ Base de données

### Connexion PostgreSQL
```
Host:     localhost
Port:     5432
Database: mini_dish_db
User:     postgres
Password: postgres
```

### Tables principales

#### `ingredient`
```sql
CREATE TABLE ingredient (
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(255),
    price    NUMERIC(10, 2),
    category ingredient_category
);
```

#### `stock_movement` ⭐ NOUVEAU TD4
```sql
CREATE TABLE stock_movement (
    id                SERIAL PRIMARY KEY,
    id_ingredient     INT REFERENCES ingredient(id),
    quantity          NUMERIC(10, 2),
    unit              unit,
    type              movement_type,
    creation_datetime TIMESTAMP WITHOUT TIME ZONE
);
```

---

## 🚀 Installation et lancement

### 1. Prérequis
- Java 21
- Maven
- PostgreSQL 12+

### 2. Configuration de la base de données

```bash
# Créer la base de données
psql -U postgres
CREATE DATABASE mini_dish_db;
\q

# Exécuter les scripts SQL
psql -U postgres -d mini_dish_db -f src/main/resources/sql/schema.sql
psql -U postgres -d mini_dish_db -f src/main/resources/sql/data.sql
```

### 3. Compiler et exécuter

```bash
# Compiler le projet
mvn clean compile

# Exécuter le Main
mvn exec:java -Dexec.mainClass="org.example.Main"
```

---

## 🧪 Test principal : Calcul du stock de la Laitue

Le programme teste le calcul du stock de la **Laitue** au **2024-01-06 12:00**.

### Mouvements de stock de la Laitue
```
2024-01-05 08:00  →  +5.0 KG  (IN)   Entrée de stock
2024-01-06 12:00  →  -0.2 KG  (OUT)  Sortie de stock
```

### Calcul attendu
```
Entrées (IN)  : 5.0 KG
Sorties (OUT) : 0.2 KG
───────────────────────
Stock         : 4.8 KG  ✅
```

### Résultat du test
```
✅ RÉSULTAT :
  Valeur attendue : 4.8 KG
  Valeur calculée : 4.8 KG
  Statut : ✓ CORRECT

🎉 Le calcul du stock est CORRECT !
```

---

## 💡 Fonctionnalités implémentées

### 1. `Ingredient.getStockValueAt(Instant t)`

Calcule le stock disponible à un instant donné.

**Algorithme :**
1. Filtrer les mouvements avant ou égal à l'instant `t`
2. Vérifier qu'il n'y a qu'une seule unité (pas de conversion)
3. Sommer les entrées (type = IN)
4. Sommer les sorties (type = OUT)
5. Retourner : `entrées - sorties`

**Exemple d'utilisation :**
```java
Ingredient laitue = dataRetriever.findIngredientById(1);
Instant date = LocalDateTime.of(2024, 1, 6, 12, 0).toInstant(ZoneOffset.UTC);
StockValue stock = laitue.getStockValueAt(date);
System.out.println("Stock : " + stock); // 4.8 KG
```

### 2. `DataRetriever.findIngredientById(Integer id)`

Récupère un ingrédient avec tous ses mouvements de stock.

```java
Ingredient ingredient = dataRetriever.findIngredientById(1);
System.out.println(ingredient);
// Ingredient{id=1, name='Laitue', category=VEGETABLE, price=800.0, actualStock=4.8 KG}
```

### 3. `DataRetriever.saveIngredient(Ingredient toSave)`

Sauvegarde un ingrédient avec ses mouvements de stock.

**Règles importantes :**
- Si `ingredient.id == null` → INSERT (création)
- Si `ingredient.id != null` → UPDATE (mise à jour)
- Pour les mouvements :
  - Si `movement.id != null` → `ON CONFLICT DO NOTHING` (pas de modification)
  - Si `movement.id == null` → INSERT (nouveau mouvement)

**Exemple :**
```java
// Créer un nouveau mouvement
StockMovement newMovement = new StockMovement();
newMovement.setType(MovementTypeEnum.IN);
newMovement.setCreationDatetime(Instant.now());

StockValue value = new StockValue(10.0, Unit.KG);
newMovement.setValue(value);

// Ajouter à l'ingrédient
ingredient.getStockMovementList().add(newMovement);

// Sauvegarder
Ingredient saved = dataRetriever.saveIngredient(ingredient);
```

---

## 📊 Données de test

### Ingrédients
| ID | Nom      | Prix (Ar/kg) | Catégorie  |
|----|----------|--------------|------------|
| 1  | Laitue   | 800.0        | VEGETABLE  |
| 2  | Tomate   | 600.0        | VEGETABLE  |
| 3  | Poulet   | 4500.0       | ANIMAL     |
| 4  | Chocolat | 3000.0       | OTHER      |
| 5  | Beurre   | 2500.0       | DAIRY      |

### Stocks attendus au 2024-01-06 12:00
| ID | Ingrédient | Stock | Calcul          |
|----|------------|-------|-----------------|
| 1  | Laitue     | 4.8   | 5.0 - 0.2       |
| 2  | Tomate     | 3.85  | 4.0 - 0.15      |
| 3  | Poulet     | 9.0   | 10.0 - 1.0*     |
| 4  | Chocolat   | 2.7   | 3.0 - 0.3*      |
| 5  | Beurre     | 2.3   | 2.5 - 0.2*      |

*Les sorties à 13:00 et 14:00 ne sont pas comptées car après 12:00

---

## ⚠️ Points importants

### 1. Pas de conversion d'unités
Pour le TD4, on ne gère PAS la conversion d'unités. Si un ingrédient a des mouvements dans plusieurs unités différentes, une exception est levée.

```java
throw new RuntimeException("Multiple units found and conversion is not handled");
```

### 2. ON CONFLICT DO NOTHING
Si on tente d'insérer un mouvement avec un ID existant, PostgreSQL ignore l'insertion silencieusement (pas d'erreur, pas de mise à jour).

```sql
INSERT INTO stock_movement (...)
VALUES (...)
ON CONFLICT (id) DO NOTHING;
```

### 3. Mouvements immuables
Les mouvements de stock ne peuvent **PAS** être modifiés ou supprimés une fois créés (règle métier).

### 4. Instant vs Timestamp
- Java utilise `java.time.Instant`
- PostgreSQL utilise `TIMESTAMP WITHOUT TIME ZONE`
- Conversion : `Timestamp.from(instant)` et `timestamp.toInstant()`

---

## 🎓 Concepts clés du TD4

### Types ENUM PostgreSQL
```sql
CREATE TYPE unit AS ENUM ('PCS', 'KG', 'L');
CREATE TYPE movement_type AS ENUM ('IN', 'OUT');
CREATE TYPE ingredient_category AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
```

### Cast PostgreSQL
```sql
-- Utiliser ::type pour caster en type ENUM
INSERT INTO stock_movement (unit, type) 
VALUES ('KG'::unit, 'IN'::movement_type);
```

### Streams Java
```java
// Filtrer et sommer avec Streams
double total = movements.stream()
    .filter(m -> m.getType().equals(MovementTypeEnum.IN))
    .mapToDouble(m -> m.getValue().getQuantity())
    .sum();
```

---

## 📝 TODO pour la suite (ANNEXE)

- [ ] Implémenter les commandes (Order, DishOrder)
- [ ] Vérifier les stocks avant de créer une commande
- [ ] Générer automatiquement les références de commande (ORD00001, ORD00002...)
- [ ] Créer les mouvements de stock lors d'une vente
- [ ] Implémenter `getTotalAmountWithoutVat()` et `getTotalAmountWithVat()`

---

## 🐛 Dépannage

### Erreur de connexion PostgreSQL
```
Erreur: connection to server at "localhost" (::1), port 5432 failed
```
**Solution :** Vérifier que PostgreSQL est démarré
```bash
# Sur Linux/Mac
sudo systemctl status postgresql

# Sur Windows
# Vérifier dans les Services Windows
```

### Erreur "Sequence not found"
```
IllegalArgumentException: No sequence found for ingredient.id
```
**Solution :** Vérifier que la table utilise SERIAL
```sql
ALTER TABLE ingredient ALTER COLUMN id TYPE SERIAL;
```

---

## 👨‍💻 Auteur
HEI - Promotion 2025
TD4 : Gestion des stocks

**Date :** Janvier 2026
