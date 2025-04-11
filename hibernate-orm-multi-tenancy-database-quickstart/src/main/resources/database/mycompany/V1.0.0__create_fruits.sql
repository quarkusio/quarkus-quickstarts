CREATE SEQUENCE fruit_seq INCREMENT BY 50; -- 50 is quarkus default
CREATE TABLE fruit
(
  id   INT,
  name VARCHAR(40)
);
INSERT INTO fruit(id, name) VALUES (1, 'Avocado');
INSERT INTO fruit(id, name) VALUES (2, 'Apricots');
INSERT INTO fruit(id, name) VALUES (3, 'Blackberries');
ALTER SEQUENCE fruit_seq RESTART WITH 4;
