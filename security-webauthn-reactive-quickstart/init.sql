CREATE ROLE quarkus WITH LOGIN PASSWORD 'quarkus';
CREATE DATABASE elytron_security_webauthn;
GRANT ALL PRIVILEGES ON DATABASE elytron_security_webauthn TO quarkus;
\c elytron_security_webauthn

