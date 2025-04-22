CREATE SEQUENCE fruit_seq INCREMENT BY 50; -- 50 is quarkus default
CREATE TABLE fruit
(
    id   INT,
    name VARCHAR(40)
);
INSERT INTO fruit(id, name) VALUES (1, 'Cherry');
INSERT INTO fruit(id, name) VALUES (2, 'Apple');
INSERT INTO fruit(id, name) VALUES (3, 'Banana');
ALTER SEQUENCE fruit_seq RESTART WITH 4;
