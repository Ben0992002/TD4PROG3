-- 1. Nettoyage (optionnel mais recommandé pour les tests)
TRUNCATE TABLE Vente, Piece_auto, Modele_voiture RESTART IDENTITY CASCADE;

-- 2. Insertion des modèles
INSERT INTO Modele_voiture (marque, modele) VALUES
                                                ('KIA', 'PRIDE'),
                                                ('HYUNDAI', 'GETZ'),
                                                ('DAEWOO', 'LACETTI');

-- 3. Insertion des pièces en récupérant l'ID dynamiquement
INSERT INTO Piece_auto (id_modele_voiture, numero_serie, prix) VALUES
                                                                   ((SELECT id FROM Modele_voiture WHERE modele = 'PRIDE'), 'SN-KIA-01', 100.0),
                                                                   ((SELECT id FROM Modele_voiture WHERE modele = 'GETZ'), 'SN-HYU-01', 150.0),
                                                                   ((SELECT id FROM Modele_voiture WHERE modele = 'LACETTI'), 'SN-DAE-01', 200.0);

-- 4. Insertion des ventes en récupérant l'ID de la pièce par son numéro de série
INSERT INTO Vente (id_piece_auto, quantite) VALUES
                                                ((SELECT id FROM Piece_auto WHERE numero_serie = 'SN-KIA-01'), 10),
                                                ((SELECT id FROM Piece_auto WHERE numero_serie = 'SN-HYU-01'), 5),
                                                ((SELECT id FROM Piece_auto WHERE numero_serie = 'SN-KIA-01'), 5);