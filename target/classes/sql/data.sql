-- Insertion des voitures (Référence)
INSERT INTO voiture (marque, modele, prix_unitaire) VALUES
                                                        ('Toyota', 'Corolla', 25000.00),
                                                        ('Toyota', 'Yaris', 18000.00),
                                                        ('Ford', 'Mustang', 45000.00);

-- Insertion des ventes (Mouvements)
-- Janvier (Mois 1)
INSERT INTO vente (id_voiture, quantite, date_vente) VALUES (1, 2, '2023-01-15');
INSERT INTO vente (id_voiture, quantite, date_vente) VALUES (2, 5, '2023-01-20');

-- Février (Mois 2)
INSERT INTO vente (id_voiture, quantite, date_vente) VALUES (1, 1, '2023-02-10');
INSERT INTO vente (id_voiture, quantite, date_vente) VALUES (3, 2, '2023-02-15');

-- Mars (Mois 3)
INSERT INTO vente (id_voiture, quantite, date_vente) VALUES (3, 1, '2023-03-05');