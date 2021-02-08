CREATE SEQUENCE known_fruits_id_seq;
SELECT setval('known_fruits_id_seq', 3);
CREATE TABLE known_fruits
(
  id   INT,
  name VARCHAR(40)
);
INSERT INTO known_fruits(id, name) VALUES (1, 'Avocado');
INSERT INTO known_fruits(id, name) VALUES (2, 'Apricots');
INSERT INTO known_fruits(id, name) VALUES (3, 'Blackberries');
