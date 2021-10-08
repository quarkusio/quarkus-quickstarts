INSERT INTO fruitEntity(id, name) VALUES (nextval('hibernate_sequence'), 'Cherry');
INSERT INTO fruitEntity(id, name) VALUES (nextval('hibernate_sequence'), 'Apple');
INSERT INTO fruitEntity(id, name) VALUES (nextval('hibernate_sequence'), 'Banana');

-- force using the same if for entity and repository to facilitate testing
INSERT INTO fruit(id, name) VALUES (1, 'Cherry');
INSERT INTO fruit(id, name) VALUES (2, 'Apple');
INSERT INTO fruit(id, name) VALUES (3, 'Banana');
