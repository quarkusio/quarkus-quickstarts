# google-cloud-functions-http project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `google-cloud-functions-http-quickstart-1.0.0-SNAPSHOT-runner.jar` file in the `/target/deployment` directory.
Be aware that it’s an _über-jar_ that include all dependencies in it.

The application can now be deployed as a function to Google Cloud Platform.