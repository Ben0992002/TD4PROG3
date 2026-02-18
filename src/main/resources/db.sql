-- 1. Création de la base de données
-- Note : À exécuter en tant qu'utilisateur 'postgres' (super-utilisateur)
SELECT 'CREATE DATABASE hei_prog3_td5'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'hei_prog3_td5')\gexec

-- 2. Connexion à la base (dans un outil CLI comme psql)
-- \c hei_prog3_td5;

-- 3. Création d'un utilisateur spécifique pour l'application
-- Rigueur : On ne se connecte jamais à Java en tant que 'postgres'.
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_user WHERE usename = 'hei_user') THEN
CREATE ROLE hei_user WITH LOGIN PASSWORD 'hei_password';
END IF;
END
$$;

-- 4. Attribution des droits sur le schéma public
GRANT ALL PRIVILEGES ON SCHEMA public TO hei_user;