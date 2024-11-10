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
INSERT INTO test_user (id, username, password, role) VALUES (1, 'admin', '$2a$10$Uc.SZ0hvGJQlYdsAp7be1.lFjmOnc7aAr4L0YY3/VN3oK.F8zJHRG', 'admin');
INSERT INTO test_user (id, username, password, role) VALUES (2, 'user','$2a$10$Uc.SZ0hvGJQlYdsAp7be1.lFjmOnc7aAr4L0YY3/VN3oK.F8zJHRG', 'user');
