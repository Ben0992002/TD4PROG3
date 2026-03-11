-- Table Modele_voiture
CREATE TABLE Modele_voiture (
                                id SERIAL PRIMARY KEY,
                                marque marque_enum,
                                modele modele_enum
);

-- Table Piece_auto
CREATE TABLE Piece_auto (
                            id SERIAL PRIMARY KEY,
                            id_modele_voiture INT REFERENCES Modele_voiture(id),
                            numero_serie VARCHAR(50),
                            prix DOUBLE PRECISION
);

-- Table Vente
CREATE TABLE Vente (
                       id SERIAL PRIMARY KEY,
                       id_piece_auto INT REFERENCES Piece_auto(id),
                       quantite INT
);

-- On s'assure que user_k2 peut manipuler les données
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO user_k2;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO user_k2;