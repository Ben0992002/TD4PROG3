-- 1. Insertion des modèles de voitures
INSERT INTO modele_voiture (marque, modele) VALUES
                                                ('FORD', 'RANGER'),
                                                ('FORD', 'EVEREST'),
                                                ('GMC', 'YUKON'),
                                                ('DODGE', 'RAM');

-- 2. Insertion des mécaniciens
-- Rakoto est spécialisé FORD, Rabe est spécialisé DODGE
INSERT INTO mecanicien (nom, marque) VALUES
                                         ('Rakoto', 'FORD'),
                                         ('Rabe', 'DODGE');

-- 3. Insertion des réparations (Données basées sur le sujet)
-- Supposons :
-- Rakoto (ID 1) répare une FORD (ID 1) pour 320 000 Ar
-- Rakoto (ID 1) répare une GMC (ID 3) pour 100 000 Ar
-- Rabe (ID 2) répare une DODGE (ID 4) pour 35 000 Ar
-- Rabe (ID 2) répare une FORD (ID 1) pour 0 Ar (ou n'a pas encore fait de profit dessus)

INSERT INTO reparation (id_mecanicien, id_modele_voiture, cout) VALUES
                                                                    (1, 1, 320000), -- Rakoto sur Ford
                                                                    (1, 3, 100000), -- Rakoto sur GMC
                                                                    (2, 4, 35000),  -- Rabe sur Dodge
                                                                    (2, 1, 0);       -- Rabe sur Ford (cas du 0 dans le sujet)

-- Ajout de quelques données pour le nombre de réparations (Question A)
INSERT INTO reparation (id_mecanicien, id_modele_voiture, cout) VALUES
                                                                    (1, 1, 50000), -- Encore une Ford Ranger
                                                                    (1, 2, 75000), -- Une Ford Everest
                                                                    (1, 2, 75000); -- Une deuxième Ford Everest