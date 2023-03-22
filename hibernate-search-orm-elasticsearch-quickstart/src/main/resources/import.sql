INSERT INTO author(id, firstname, lastname) VALUES (1, 'John', 'Irving');
INSERT INTO author(id, firstname, lastname) VALUES (2, 'Paul', 'Auster');
alter sequence Author_SEQ restart with 3;

INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'The World According to Garp', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'The Hotel New Hampshire', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'The Cider House Rules', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'A Prayer for Owen Meany', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'Last Night in Twisted River', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'In One Person', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'Avenue of Mysteries', 1);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'The New York Trilogy', 2);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'Mr. Vertigo', 2);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'The Brooklyn Follies', 2);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'Invisible', 2);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), 'Sunset Park', 2);
INSERT INTO book(id, title, author_id) VALUES (nextval('Book_SEQ'), '4 3 2 1', 2);
