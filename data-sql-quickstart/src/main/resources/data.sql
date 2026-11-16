DELETE FROM Book;
DELETE FROM Publisher;

INSERT INTO Publisher (id, name, country, allowsDiscounts) VALUES (1, 'Addison-Wesley', 'US', true);
INSERT INTO Publisher (id, name, country, allowsDiscounts) VALUES (2, 'Prentice Hall', 'US', false);
INSERT INTO Publisher (id, name, country, allowsDiscounts) VALUES (3, 'O''Reilly', 'UK', true);
INSERT INTO Publisher (id, name, country, allowsDiscounts) VALUES (4, 'Manning', 'UK', false);

INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (1, 'Effective Java', '978-0134685991', 45.00, 1);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (2, 'Clean Code', '978-0132350884', 40.00, 1);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (3, 'Domain-Driven Design', '978-0321125217', 55.00, 2);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (4, 'Refactoring', '978-0134757599', 50.00, 2);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (5, 'The Pragmatic Programmer', '978-0135957059', 42.00, 3);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (6, 'Head First Design Patterns', '978-0596007126', 38.00, 3);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (7, 'Java Concurrency in Practice', '978-0321349606', 48.00, 4);
INSERT INTO Book (id, title, isbn, price, publisher_id) VALUES (8, 'Kotlin in Action', '978-1617293290', 44.00, 4);

-- Set each sequence to the largest imported ID so the next generated ID does not collide.
SELECT setval('Publisher_SEQ', (SELECT MAX(id) FROM Publisher));
SELECT setval('Book_SEQ', (SELECT MAX(id) FROM Book));
