CREATE SEQUENCE IF NOT EXISTS Publisher_SEQ;

CREATE TABLE IF NOT EXISTS Publisher (
    id BIGINT PRIMARY KEY DEFAULT nextval('Publisher_SEQ'),
    name VARCHAR(255),
    country VARCHAR(10),
    allowsDiscounts BOOLEAN
);

CREATE SEQUENCE IF NOT EXISTS Book_SEQ;

CREATE TABLE IF NOT EXISTS Book (
    id BIGINT PRIMARY KEY DEFAULT nextval('Book_SEQ'),
    title VARCHAR(255),
    isbn VARCHAR(20),
    price NUMERIC(10,2),
    publisher_id BIGINT REFERENCES Publisher(id)
);
