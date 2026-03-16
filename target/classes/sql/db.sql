-- Création de l'utilisateur dédié
CREATE USER k2_admin WITH PASSWORD 'k2_password';

-- Création de la base
CREATE DATABASE examen_k2;

-- Donner les droits de connexion et de modification à cet utilisateur
GRANT ALL PRIVILEGES ON DATABASE examen_k2 TO k2_admin;

-- Se connecter à la base pour donner les droits sur le schéma (PostgreSQL 15+)
\c examen_k2;
GRANT ALL ON SCHEMA public TO k2_admin;

-- Donne tous les droits sur toutes les tables existantes à k2_admin
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO k2_admin;

-- Donne les droits sur les séquences (nécessaire pour les ID auto-incrémentés)
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO k2_admin;