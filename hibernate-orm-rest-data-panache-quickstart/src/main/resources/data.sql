-- Entity sample
INSERT INTO personForEntity(id, name, birthDate) VALUES (1, 'John Johnson', '1988-01-10');
INSERT INTO personForEntity(id, name, birthDate) VALUES (2, 'Peter Peterson', '1986-11-20');
ALTER SEQUENCE personForEntity_seq RESTART WITH 3;
-- Repository sample
INSERT INTO personForRepository(name, birthDate) VALUES ('John Johnson', '1988-01-10');
INSERT INTO personForRepository(name, birthDate) VALUES ('Peter Peterson', '1986-11-20');
