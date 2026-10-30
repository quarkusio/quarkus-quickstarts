INSERT INTO Greeting(id, name) VALUES (1, 'Alice');
INSERT INTO Greeting(id, name) VALUES (2, 'Bob');

-- Set the sequence to the largest imported ID so the next generated ID does not collide.
SELECT setval('Greeting_SEQ', (SELECT MAX(id) FROM Greeting));
