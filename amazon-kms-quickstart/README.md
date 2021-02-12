# Quarkus demo: Amazon KMS Client

This example showcases how to use the AWS KMS client with Quarkus. As a prerequisite install Install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# AWS KMS local instance

Just run it as follows in order to start KMS locally:
`docker run --rm --name local-kms -p 8011:4599 -e SERVICES=kms -e START_WEB=0 -d localstack/localstack:0.11.1`
KMS listens on `localhost:8011` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Create KMS master key

Create a master key and store the ARN in the `MASTER_KEY_ARN` environment variable
```
$> MASTER_KEY_ARN=`aws kms create-key --profile localstack --endpoint-url=http://localhost:8011 | cut -f3`
```
Generate key data as 256-bit symmetric key (AES_256)
```
$> aws kms generate-data-key --key-id $MASTER_KEY_ARN --key-spec AES_256 --profile localstack --endpoint-url=http://localhost:8011
```

# Run the demo on dev mode

- Run `./mvnw clean package` and then `java -Dkey.arn=$MASTER_KEY_ARN -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev -Dkey.arn=$MASTER_KEY_ARN`

## Encrypt your text
```
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/sync/encrypt -d'Quarkus is awsome'
```
And the result similar to this output:
```
S2Fybjphd3M6a21zOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6a2V5LzZmYzAwOWZhLWYwMDUtNGI4My04ZDc1LTk4OGVhZTk4ZTM1NwAAAAAfC2HyHrHBXLNFomHLdH9PlMKWQKofyhJjbY2TUovEaBuc4Hj+Lb2BSoYTa/c=
```
## Decrypt your message
You can now decrypt a message you previously encrypted

```
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/sync/decrypt -d '<encrypted-message>'
```

Repeat the same using async endpoints. Encrypt
```
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/encrypt -d 'Quarkus is awsome'
```
And then decrypt
```
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/decrypt -d '<encrypted-message>'
```

# Running in native

You can compile the application into a native binary using:

`./mvnw clean install -Pnative`

and run with:

`./target/amazon-kms-quickstart-1.0.0-SNAPSHOT-runner -Dkey.arn=$MASTER_KEY_ARN` 


# Running native in container

Build a native image in container by running:
`./mvnw package -Pnative -Dnative-image.docker-build=true`

Build a docker image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-kms-quickstart .`

Create a network that connect your container with localstack
`docker network create localstack`

Stop your localstack container you started at the beginning
`docker stop local-kms`

Start localstack and connect to the network
`docker run --rm --network=localstack --name localstack -p 8011:4599 -e SERVICES=kms -e START_WEB=0 -d localstack/localstack:0.11.1`

Create a master key and store the ARN in the `CMK_ARN` environment variable
```
$> MASTER_KEY_ARN=`aws kms create-key --profile localstack --endpoint-url=http://localhost:8011 | cut -f3`
```
Generate key data as 256-bit symmetric key (AES_256)
```
$> aws kms generate-data-key --key-id $MASTER_KEY_ARN --key-spec AES_256 --profile localstack --endpoint-url=http://localhost:8011
```
Run quickstart container connected to that network (note that we're using internal port of the localstack)
`docker run -i --rm --network=localstack -p 8080:8080 quarkus/amazon-kms-quickstart -Dquarkus.kms.endpoint-override=http://localstack:4599`
