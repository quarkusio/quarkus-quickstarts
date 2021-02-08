# Quarkus demo: Amazon SES Client

This example showcases how to use the AWS SES client with Quarkus. As a prerequisite install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html). 
Despite the fact, this example uses local AWS SES for integration test purposes, we encourage you to use SES from the AWS account as it allows you to send emails. 
Local instance of SES only mocks service APIs and doesn't send any emails.

# AWS SES local instance

Just run it as follows in order to start SES locally:
`docker run --rm --name local-ses -p 8012:4579 -e SERVICES=ses -e START_WEB=0 -d localstack/localstack:0.11.1`
SES listens on `localhost:8012` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Setup AWS SES

Verify the email addresses you're going to use when running the application. You need to verify both the sender and recipient email addresses.
```
aws ses verify-email-identity --email-address <EMAIL_ADDRESS> --profile localstack --endpoint-url=http://localhost:8012
```
If you're about to use your AWS account instead, verify all email addresses using your account profile.
```
aws ses verify-email-identity --email-address <EMAIL_ADDRESS>
```
You need to repeat that command for each email address you're going to use.

# Run the demo on dev mode

If you want to run the demo using your AWS account, comment out all the `quarkus.ses` properties, so your default profile AWS key and region will be used.

- Run `./mvnw clean package` and then `java -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev`

## Send an email
Using sync endpoint
```
curl -XPOST -H"Content-type: application/json" http://localhost:8080/sync/email -d'{"from": "<EMAIL>", "to": "<EMAIL>", "subject": "Hello from Quarkus", "body": "Quarkus is awsome"}'
```
Or async endpoint
```
curl -XPOST -H"Content-type: application/json" http://localhost:8080/async/email -d'{"from": "<EMAIL>", "to": "<EMAIL>", "subject": "Hello from Quarkus", "body": "Quarkus is awsome"}'
```

As a result, you will see the ID of the message as SES returned. E.g.:
```
010701724bec5607-e34882d5-a8ce-4f2b-a837-da75989e43c0-000000
```

Check inbox of the recipient email address for an email you just sent.

# Running in native

You can compile the application into a native binary using:

`./mvnw clean install -Pnative`

and run with:

`./target/amazon-ses-quickstart-1.0.0-SNAPSHOT-runner` 
