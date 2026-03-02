-- Suppression pour repartir à neuf
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS voter;
DROP TABLE IF EXISTS candidate;
DROP TYPE IF EXISTS vote_type;

-- Structure
CREATE TYPE vote_type AS ENUM ('VALID', 'BLANK', 'NULL');

CREATE TABLE candidate (
                           id SERIAL PRIMARY KEY,
                           name TEXT NOT NULL
);

CREATE TABLE voter (
                       id SERIAL PRIMARY KEY,
                       name TEXT NOT NULL
);

CREATE TABLE vote (
                      id SERIAL PRIMARY KEY,
                      candidate_id INT REFERENCES candidate(id),
                      voter_id INT NOT NULL REFERENCES voter(id),
                      vote_type vote_type NOT NULL
);