-- 1. Nettoyage des données existantes (pour éviter les doublons lors des tests)
TRUNCATE TABLE tax_config, invoice_line, invoice RESTART IDENTITY CASCADE;

-- 2. Insertion des Factures
INSERT INTO invoice (customer_name, status) VALUES
(1, 'Alice', 'CONFIRMED'),
(2, 'Bob', 'PAID'),
(3, 'Charlie', 'DRAFT');
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
                                                                       (1, 'Produit A', 2, 100),
                                                                       (1, 'Produit B', 1, 50),
                                                                       (2, 'Produit A', 5, 100),
                                                                       (2, 'Service C', 1, 200),
                                                                       (3, 'Produit B', 3, 50);

-- 3. Insertion des Lignes de Facture
-- Facture 1 (Alice) : 2*100 + 1*50 = 250.00
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
                                                                       (1, 'Produit A', 2, 100.00),
                                                                       (1, 'Produit B', 1, 50.00);

-- Facture 2 (Bob) : 5*100 + 1*200 = 700.00
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
                                                                       (2, 'Produit A', 5, 100.00),
                                                                       (2, 'Service C', 1, 200.00);

-- Facture 3 (Charlie) : 3*50 = 150.00
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
    (3, 'Produit B', 3, 50.00);

-- 4. Insertion de la Configuration Fiscale (Q5)
INSERT INTO tax_config (label, rate) VALUES
    ('TVA STANDARD', 20.00);
