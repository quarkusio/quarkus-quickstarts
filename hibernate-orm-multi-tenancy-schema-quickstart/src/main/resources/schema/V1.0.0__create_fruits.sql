CREATE SEQUENCE base.known_fruits_id_seq;
SELECT setval('base."known_fruits_id_seq"', 3);
CREATE TABLE base.known_fruits
(
  id   INT,
  name VARCHAR(40)
);
INSERT INTO base.known_fruits(id, name) VALUES (1, 'Cherry');
INSERT INTO base.known_fruits(id, name) VALUES (2, 'Apple');
INSERT INTO base.known_fruits(id, name) VALUES (3, 'Banana');

CREATE SEQUENCE mycompany.known_fruits_id_seq;
SELECT setval('mycompany."known_fruits_id_seq"', 3);
CREATE TABLE mycompany.known_fruits
(
  id   INT,
  name VARCHAR(40)
);
INSERT INTO mycompany.known_fruits(id, name) VALUES (1, 'Avocado');
INSERT INTO mycompany.known_fruits(id, name) VALUES (2, 'Apricots');
INSERT INTO mycompany.known_fruits(id, name) VALUES (3, 'Blackberries');
