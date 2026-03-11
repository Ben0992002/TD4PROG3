-- Suppression si existant pour repartir à neuf (optionnel)
-- DROP DATABASE IF EXISTS garage_k2;
-- DROP USER IF EXISTS user_k2;

-- Création de la base
CREATE DATABASE garage_k2;

-- Création de l'utilisateur dédié
CREATE USER user_k2 WITH PASSWORD 'passwordk2';
GRANT ALL PRIVILEGES ON DATABASE garage_k2 TO user_k2;

-- Connexion à la base pour créer les types
\c garage_k2

-- Création des types ENUM
CREATE TYPE marque_enum AS ENUM ('KIA', 'HYUNDAI', 'DAEWOO');
CREATE TYPE modele_enum AS ENUM ('GETZ', 'PRIDE', 'LACETTI');

-- Permissions sur le schéma pour que user_k2 puisse créer les tables
GRANT ALL ON SCHEMA public TO user_k2;