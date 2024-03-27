CREATE TABLE quarkus_jbosststxtable (statetype integer not null, hidden integer not null, typename character varying(255) not null, uidstring character varying(255) not null, objectstate bytea);
CREATE TABLE quarkus_jbosststxtable_historical_data AS SELECT * FROM quarkus_jbosststxtable;
CREATE OR REPLACE FUNCTION object_store_historical_data() RETURNS TRIGGER AS '
    BEGIN
        INSERT INTO quarkus_jbosststxtable_historical_data(statetype, hidden, typename, uidstring, objectstate) VALUES (NEW.statetype, NEW.hidden, NEW.typename, NEW.uidstring, NEW.objectstate);
        RETURN NULL;
    END;
' LANGUAGE plpgsql;
CREATE TRIGGER object_store_historical_data_trigger AFTER INSERT ON quarkus_jbosststxtable FOR EACH ROW EXECUTE FUNCTION object_store_historical_data();
CREATE TABLE audit_log (id BIGSERIAL PRIMARY KEY, message VARCHAR(10), datasource VARCHAR(40));
CREATE TABLE example (data VARCHAR(6) NOT NULL);
INSERT INTO example VALUES ('first');
INSERT INTO example VALUES ('second');
INSERT INTO example VALUES ('third');