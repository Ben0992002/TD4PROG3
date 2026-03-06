CREATE TYPE marque_enum AS ENUM ('FORD', 'GMC', 'DODGE');
CREATE TYPE modele_enum AS ENUM ('RANGER', 'EVEREST', 'YUKON', 'RAM');

CREATE TABLE modele_voiture (
                                id SERIAL PRIMARY KEY,
                                marque marque_enum NOT NULL,
                                modele modele_enum NOT NULL
);

CREATE TABLE mecanicien (
                            id SERIAL PRIMARY KEY,
                            nom VARCHAR(100) NOT NULL,
                            marque marque_enum NOT NULL
);

CREATE TABLE reparation (
                            id SERIAL PRIMARY KEY,
                            id_mecanicien INT REFERENCES mecanicien(id),
                            id_modele_voiture INT REFERENCES modele_voiture(id),
                            cout FLOAT NOT NULL
);