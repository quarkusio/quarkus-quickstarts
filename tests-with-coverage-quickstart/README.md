# Measuring the coverage of your tests

Learn how to measure the test coverage of your application.

* Measuring the coverage of your Unit Tests
* Measuring the coverage of your Integration Tests
* Separating the execution of your Unit Tests and Integration Tests
* Consolidating the coverage for all your tests

Please note that code coverage is not supported in native mode.

## How to run

You need:

* JDK 1.8+ installed with JAVA_HOME configured appropriately
* Apache Maven 3.5.3+

This way, the ```QuarkusTest``` instances will be executed as part of the ```integration-test``` build phase while the other JUnit tests will still be ran during the ```test``` phase.

You can all the tests via the command ```mvn clean verify``` (and you will notice that the two tests are ran at different times).

It will generate three separate reports:
* a report of the coverage of the unit tests in ```target/site/jacoco-ut```
* a report of the coverage of the integration tests in ```target/site/jacoco-it```
* a consolidated report that will show the coverage of all your tests combined in  ```target/site/jacoco```

## Learn more

For more in-depth information please see [Quarkus - Measuring the coverage of your tests](https://github.com/quarkusio/quarkus/blob/master/docs/src/main/asciidoc/tests-with-coverage.adoc) guide.
