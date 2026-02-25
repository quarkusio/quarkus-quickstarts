ALTER SEQUENCE movie_seq RESTART WITH 50;

INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'The Matrix', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'Inception', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'Interstellar', 4);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'The Godfather', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'Pulp Fiction', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'The Shawshank Redemption', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'Fight Club', 4);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'Forrest Gump', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'The Dark Knight', 5);
INSERT INTO Movie (id, title, rating) VALUES (nextval('movie_seq'), 'The Lord of the Rings', 5);