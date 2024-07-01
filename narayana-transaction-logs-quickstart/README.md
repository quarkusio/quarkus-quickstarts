Quarkus - Narayana - Transaction logs
========================

This QuickStart demonstrates how your Quarkus application can be configured for automatic transaction recovery.

## Test crash recovery

- build application: 
  - `./mvnw clean package -DskipTests -DskipITs`
- remove previous database volumes, so that we start with a clean sheet:
  - `docker-compose down --volumes`
- start database: 
  - `docker compose up`
- wait until container has started and is ready to accept connections
- start application: 
  - `java -jar ./target/quarkus-app/quarkus-run.jar`
- send a message to make transaction and crash application: 
  - `curl -v localhost:8080/transaction-logs/transaction-recovery`
- inspect JDBC Object Store:
  - connect to Postgres container:
    - execute `docker exec -it $(docker ps | grep 'postgres' | awk '{print $1}') psql -U quarkus_test -W narayana_transaction_logs_db`
    - enter password `quarkus_test`
  - now you can see that JDBC object store contains transaction waiting for recovery:
    - enter `SELECT * FROM quarkus_jbosststxtable;`
  - table we were trying to insert when application crashed is empty:
    - enter `SELECT * FROM audit_log;`
- start application again:
  - `java -jar ./target/quarkus-app/quarkus-run.jar`
- in a moment, you will see that the `audit_log` database table contains 2 inserts:
  - enter `SELECT * FROM audit_log;`
    ```
     id | message |   datasource    
    ----+---------+-----------------
      1 | crash   | <default>
      2 | crash   | object-store-ds
    ```