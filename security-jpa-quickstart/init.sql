CREATE ROLE quarkus WITH LOGIN PASSWORD 'quarkus';
CREATE DATABASE elytron_security_jpa;
GRANT ALL PRIVILEGES ON DATABASE elytron_security_jpa TO quarkus;
\c elytron_security_jpa

