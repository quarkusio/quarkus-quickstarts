INSERT INTO author(id, firstname, lastname) VALUES (1, 'John', 'Irving');
INSERT INTO author(id, firstname, lastname) VALUES (2, 'Paul', 'Auster');
ALTER SEQUENCE author_seq RESTART WITH 3;

INSERT INTO book(id, title, author_id) VALUES (1, 'The World According to Garp', 1);
INSERT INTO book(id, title, author_id) VALUES (2, 'The Hotel New Hampshire', 1);
INSERT INTO book(id, title, author_id) VALUES (3, 'The Cider House Rules', 1);
INSERT INTO book(id, title, author_id) VALUES (4, 'A Prayer for Owen Meany', 1);
INSERT INTO book(id, title, author_id) VALUES (5, 'Last Night in Twisted River', 1);
INSERT INTO book(id, title, author_id) VALUES (6, 'In One Person', 1);
INSERT INTO book(id, title, author_id) VALUES (7, 'Avenue of Mysteries', 1);
INSERT INTO book(id, title, author_id) VALUES (8, 'The New York Trilogy', 2);
INSERT INTO book(id, title, author_id) VALUES (9, 'Mr. Vertigo', 2);
INSERT INTO book(id, title, author_id) VALUES (10, 'The Brooklyn Follies', 2);
INSERT INTO book(id, title, author_id) VALUES (11, 'Invisible', 2);
INSERT INTO book(id, title, author_id) VALUES (12, 'Sunset Park', 2);
INSERT INTO book(id, title, author_id) VALUES (13, '4 3 2 1', 2);
ALTER SEQUENCE book_seq RESTART WITH 14;
