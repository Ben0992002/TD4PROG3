-- 1. Nettoyage (Optimal, utile pour les tests)
DROP TABLE IF EXISTS tax_config CASCADE;
DROP TABLE IF EXISTS invoice_line CASCADE;
DROP TABLE IF EXISTS invoice CASCADE;
DROP TYPE IF EXISTS invoice_status;

-- 2. Définition des types énumérés
CREATE TYPE invoice_status AS ENUM(DRAFT', 'CONFIRMED', 'PAID');

-- 3. Table des factures
CREATE TABLE invoice (
id SERIAL PRIMARY KEY,
customer_name VARCHAR(255) NOT NULL,
status invoice_status);

-- 4. Table des Lignes de facture (Détails)
CREATE TABLE invoice_line (
id SERIAL PRIMARY KEY,
invoice_id INT NOT NULL REFERENCES invoice(id),
label VARCHAR(255) NOT NULL,
quantity INT NOT NULL,
unit_price NUMERIC(10,2) NOT NULL
);

--5. Table de configuration Fiscale
CREATE TABLE tax_config (
    id SERIAL PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    rate NUMERIC(5,2) NOT NULL
);