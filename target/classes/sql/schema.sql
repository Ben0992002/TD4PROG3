-- Suppression des tables si elles existent (pour repartir à zéro)
DROP TABLE IF EXISTS vente;
DROP TABLE IF EXISTS voiture;

-- Table des modèles de voitures
CREATE TABLE voiture (
                         id_voiture SERIAL PRIMARY KEY,
                         marque VARCHAR(50) NOT NULL,
                         modele VARCHAR(50),
                         prix_unitaire DECIMAL(10, 2) NOT NULL
);

-- Table des ventes liées aux voitures
CREATE TABLE vente (
                       id_vente SERIAL PRIMARY KEY,
                       id_voiture INT REFERENCES voiture(id_voiture),
                       quantite INT NOT NULL,
                       date_vente DATE DEFAULT CURRENT_DATE
);