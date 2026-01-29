package org.example;

import org.example.model.Ingredient;
import org.example.model.StockValue;
import org.example.repository.DataRetriever;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Classe principale pour tester le TD4
 * Test du calcul de stock de la Laitue au 2024-01-06 12:00 → attendu: 4.8 KG
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("TD4 - TEST : Calcul du stock de la Laitue");
        System.out.println("=".repeat(80));

        DataRetriever dataRetriever = new DataRetriever();

        try {
            // 1. Récupérer l'ingrédient Laitue (ID = 1)
            System.out.println("\n📦 Récupération de la Laitue...");
            Ingredient laitue = dataRetriever.findIngredientById(1);
            System.out.println("✓ Laitue trouvée : " + laitue.getName());
            System.out.println("  Prix d'achat : " + laitue.getPrice() + " Ar/kg");
            System.out.println("  Catégorie : " + laitue.getCategory());
            System.out.println("  Nombre de mouvements : " + laitue.getStockMovementList().size());

            // 2. Afficher les mouvements de stock
            System.out.println("\n📊 Mouvements de stock de la Laitue :");
            System.out.println("  " + "-".repeat(70));
            System.out.println("  Date/Heure          | Type | Quantité");
            System.out.println("  " + "-".repeat(70));
            laitue.getStockMovementList().forEach(movement -> {
                System.out.printf("  %s | %-4s | %s%n",
                        movement.getCreationDatetime(),
                        movement.getType(),
                        movement.getValue());
            });

            // 3. Calculer le stock au 2024-01-06 12:00
            System.out.println("\n🔢 Calcul du stock au 2024-01-06 12:00 :");
            Instant testTime = LocalDateTime.of(2024, 1, 6, 12, 0)
                    .toInstant(ZoneOffset.UTC);

            StockValue stock = laitue.getStockValueAt(testTime);

            System.out.println("  Date de référence : 2024-01-06 12:00");
            System.out.println("  " + "-".repeat(70));

            // Détail du calcul
            double entrees = laitue.getStockMovementList().stream()
                    .filter(m -> !m.getCreationDatetime().isAfter(testTime))
                    .filter(m -> m.getType().name().equals("IN"))
                    .mapToDouble(m -> m.getValue().getQuantity())
                    .sum();

            double sorties = laitue.getStockMovementList().stream()
                    .filter(m -> !m.getCreationDatetime().isAfter(testTime))
                    .filter(m -> m.getType().name().equals("OUT"))
                    .mapToDouble(m -> m.getValue().getQuantity())
                    .sum();

            System.out.println("  Entrées (IN)  : " + entrees + " KG");
            System.out.println("  Sorties (OUT) : " + sorties + " KG");
            System.out.println("  " + "-".repeat(70));
            System.out.println("  Stock calculé : " + stock.getQuantity() + " " + stock.getUnit());

            // 4. Vérifier le résultat attendu
            double expected = 4.8;
            double calculated = stock.getQuantity();
            boolean isCorrect = Math.abs(calculated - expected) < 0.01;

            System.out.println("\n✅ RÉSULTAT :");
            System.out.println("  Valeur attendue : " + expected + " KG");
            System.out.println("  Valeur calculée : " + calculated + " KG");
            System.out.println("  Statut : " + (isCorrect ? "✓ CORRECT" : "✗ INCORRECT"));

            if (isCorrect) {
                System.out.println("\n🎉 Le calcul du stock est CORRECT !");
            } else {
                System.err.println("\n❌ ERREUR : Le calcul du stock est INCORRECT !");
                System.err.println("   Différence : " + Math.abs(calculated - expected) + " KG");
            }

            // 5. Test avec le stock actuel (maintenant)
            System.out.println("\n📅 Stock actuel (maintenant) :");
            StockValue currentStock = laitue.getStockValueAt(Instant.now());
            System.out.println("  Stock : " + currentStock);

            System.out.println("\n" + "=".repeat(80));

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- SECTION TEST EXAMEN K3 ---
System.out.println("\n🚀 TEST EXAMEN : Tentative de commande...");
try {
        Order newOrder = new Order();
        newOrder.setReference("ORD00001");
        newOrder.setType(OrderTypeEnum.TAKE_AWAY);
        newOrder.setStatus(OrderStatusEnum.CREATED);
        newOrder.setCreationDatetime(Instant.now());


        dataRetriever.saveOrder(newOrder);
        System.out.println("✅ Commande enregistrée !");

        // Test de la sécurité DELIVERED
        newOrder.setStatus(OrderStatusEnum.DELIVERED);
        System.out.println("Statut passé à DELIVERED.");

        // Cette ligne doit faire planter le programme
        newOrder.setStatus(OrderStatusEnum.READY);

    } catch (RuntimeException e) {
        System.out.println("✅ SÉCURITÉ OK : " + e.getMessage());
    }
}
