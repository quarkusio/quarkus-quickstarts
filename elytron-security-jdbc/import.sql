CREATE ROLE quarkus WITH LOGIN PASSWORD 'quarkus';
CREATE DATABASE elytron_security_jdbc;
GRANT ALL PRIVILEGES ON DATABASE elytron_security_jdbc TO quarkus;
\c elytron_security_jdbc

CREATE TABLE test_user (
    id INT,
    username VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255)
);
GRANT ALL PRIVILEGES ON TABLE  test_user TO quarkus;
INSERT INTO test_user (id, username, password, role) VALUES (1, 'admin', 'admin', 'admin');
INSERT INTO test_user (id, username, password, role) VALUES (2, 'user','user', 'user');
