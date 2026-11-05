# Native SQL queries with Quarkus Data

This quickstart is the completed example from the
[Native SQL queries with Quarkus Data tutorial](https://quarkus.io/guides/quarkus-data-hibernate-sql).

It uses Quarkus Data Hibernate to query and update a PostgreSQL database with
`@NativeQuery`, without mapped entities. The annotation processor generates an
implementation of `Library`, and query results are returned as `Library.Book`
Java records.

## Requirements

- JDK 21+
- A container runtime supported by [Dev Services](https://quarkus.io/guides/dev-services)
- Locally built Quarkus `999-SNAPSHOT` artifacts, as required by this repository's `development` branch

## Run the tests

From the repository root, run:

```shell
./mvnw clean verify -pl data-sql-quickstart
```

Dev Services automatically starts PostgreSQL. Hibernate ORM runs `import.sql`
to create the tables and sequences, followed by `data.sql` to load the sample
books and publishers and initialize the sequences from the imported IDs.
No database connection configuration is needed.

`LibraryTest` covers:

- Finding a book by title and mapping the result to a Java record.
- Returning an empty list for an unknown title.
- Updating a book's price and reading it back within a transaction.

The update test uses `@TestTransaction`, so its changes are rolled back after
the test. SQL syntax and column names are validated by the database when queries
execute, making these tests important.

## Continuous testing

From the repository root, run:

```shell
./mvnw -pl data-sql-quickstart quarkus:test
```

Quarkus re-runs the tests as you edit `Library`, the SQL scripts, or the tests.
This example is exercised through JVM tests with CDI injection and transactional
rollback, just like the tutorial.
