# 📁 ARBORESCENCE COMPLÈTE DU TD4

```
td4-complete/
│
├── 📄 pom.xml                                    Maven configuration (Java 21 + PostgreSQL)
├── 📄 README.md                                  Documentation complète du projet
│
└── src/main/
    │
    ├── java/org/example/
    │   │
    │   ├── 📄 Main.java                          ⭐ Point d'entrée - Test Laitue 4.8 KG
    │   │
    │   ├── model/                                📦 Entités et énumérations
    │   │   ├── 📄 Unit.java                      Enum: PCS, KG, L
    │   │   ├── 📄 MovementTypeEnum.java          Enum: IN, OUT
    │   │   ├── 📄 CategoryEnum.java              Enum: VEGETABLE, ANIMAL, MARINE, DAIRY, OTHER
    │   │   ├── 📄 StockValue.java                Quantity + Unit
    │   │   ├── 📄 StockMovement.java             ID, Value, Type, CreationDatetime
    │   │   └── 📄 Ingredient.java                ID, Name, Price, Category, List<StockMovement>
    │   │                                          + getStockValueAt(Instant t) ⭐
    │   │
    │   └── repository/                           📦 Couche d'accès aux données
    │       ├── 📄 DBConnection.java              Gestionnaire de connexion PostgreSQL
    │       └── 📄 DataRetriever.java             CRUD Ingredient + Stock
    │                                              + findIngredientById(Integer id)
    │                                              + saveIngredient(Ingredient) avec ON CONFLICT
    │                                              + findStockMovementsByIngredientId(Integer)
    │
    └── resources/sql/                            📦 Scripts SQL
        ├── 📄 db.sql                             Création de la base
        ├── 📄 schema.sql                         Schéma des tables
        └── 📄 data.sql                           Données de test
```

---

## 🎯 Fichiers clés

### ⭐ Main.java
Point d'entrée du programme. Teste le calcul du stock de la Laitue au 2024-01-06 12:00.
**Résultat attendu :** 4.8 KG ✅

### ⭐ Ingredient.java
Contient la méthode principale du TD4 :
```java
public StockValue getStockValueAt(Instant t)
```
Cette méthode calcule le stock disponible à un instant donné en sommant les entrées (IN) et en soustrayant les sorties (OUT).

### ⭐ DataRetriever.java
Contient les méthodes CRUD pour gérer les ingrédients et leurs stocks :
- `findIngredientById(Integer id)` : Récupère un ingrédient avec ses mouvements
- `saveIngredient(Ingredient)` : Sauvegarde avec `ON CONFLICT DO NOTHING`
- `findStockMovementsByIngredientId(Integer)` : Récupère tous les mouvements

---

## 📊 Relations entre les fichiers

```
Main.java
   │
   └──► DataRetriever.java
          │
          ├──► findIngredientById(1)
          │       │
          │       ├──► SELECT FROM ingredient WHERE id = 1
          │       │
          │       └──► findStockMovementsByIngredientId(1)
          │               │
          │               └──► SELECT FROM stock_movement WHERE id_ingredient = 1
          │
          └──► Returns: Ingredient
                  │
                  ├── id: 1
                  ├── name: "Laitue"
                  ├── price: 800.0
                  ├── category: VEGETABLE
                  └── stockMovementList: [
                          StockMovement{id=1, type=IN, quantity=5.0, date=2024-01-05 08:00},
                          StockMovement{id=2, type=OUT, quantity=0.2, date=2024-01-06 12:00}
                      ]

Ingredient.getStockValueAt(2024-01-06 12:00)
   │
   ├──► Filter movements <= 2024-01-06 12:00
   │       └──► [Movement 1 (IN, 5.0), Movement 2 (OUT, 0.2)]
   │
   ├──► Sum IN movements  → 5.0 KG
   ├──► Sum OUT movements → 0.2 KG
   │
   └──► Return: StockValue{quantity=4.8, unit=KG} ✅
```

---

## 🗄️ Tables de la base de données

```
📦 ingredient
├── id (SERIAL PK)
├── name
├── price
└── category

📦 stock_movement ⭐ TD4
├── id (SERIAL PK)
├── id_ingredient (FK → ingredient.id)
├── quantity
├── unit (ENUM)
├── type (ENUM: IN/OUT)
└── creation_datetime
```

---

## ✅ Checklist de vérification

### Fichiers Java
- [x] Unit.java (Enum)
- [x] MovementTypeEnum.java (Enum)
- [x] CategoryEnum.java (Enum)
- [x] StockValue.java (Model)
- [x] StockMovement.java (Model)
- [x] Ingredient.java (Model + getStockValueAt)
- [x] DBConnection.java (Repository)
- [x] DataRetriever.java (Repository + CRUD)
- [x] Main.java (Test)

### Configuration
- [x] pom.xml (Maven + PostgreSQL dependency)
- [x] README.md (Documentation complète)

### SQL
- [x] db.sql (Création base)
- [x] schema.sql (Tables + ENUM)
- [x] data.sql (Données de test)

---

## 🚀 Commandes utiles

```bash
# Compiler
mvn clean compile

# Exécuter
mvn exec:java -Dexec.mainClass="org.example.Main"

# Package
mvn package

# Clean
mvn clean
```

---

**Projet TD4 complet et fonctionnel ! ✅**
