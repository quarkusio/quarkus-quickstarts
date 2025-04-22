CREATE SEQUENCE base.fruit_seq INCREMENT BY 50; -- 50 is quarkus default
CREATE TABLE base.fruit
(
    id   INT,
    name VARCHAR(40)
);
INSERT INTO base.fruit(id, name) VALUES (1, 'Cherry');
INSERT INTO base.fruit(id, name) VALUES (2, 'Apple');
INSERT INTO base.fruit(id, name) VALUES (3, 'Banana');
ALTER SEQUENCE base.fruit_seq RESTART WITH 4;

CREATE SEQUENCE mycompany.fruit_seq INCREMENT BY 50; -- 50 is quarkus default
CREATE TABLE mycompany.fruit
(
  id   INT,
  name VARCHAR(40)
);
INSERT INTO mycompany.fruit(id, name) VALUES (1, 'Avocado');
INSERT INTO mycompany.fruit(id, name) VALUES (2, 'Apricots');
INSERT INTO mycompany.fruit(id, name) VALUES (3, 'Blackberries');
ALTER SEQUENCE mycompany.fruit_seq RESTART WITH 4;