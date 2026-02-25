-- 1. On se connecte d'abord à la base par défaut 'postgres' pour pouvoir créer la nôtre
-- Utile pour réinitialiser le projet sans stress
-- DROP DATABASE IF EXISTS election_db;

--2. Crée la base de données
CREATE DATABASE election_db;

-- Optionnel : on Crée un utilisateur spécifique pour ne pas utiliser 'postgres'
CREATE USER election_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE election_db TO election_user;