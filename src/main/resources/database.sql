CREATE DATABASE garage_db;
CREATE USER garage_user WITH PASSWORD 'garage_pass';
GRANT ALL PRIVILEGES ON DATABASE garage_db TO garage_user;

\c garage_db
GRANT ALL ON SCHEMA public TO garage_user;