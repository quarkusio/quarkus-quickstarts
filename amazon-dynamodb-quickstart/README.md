# Quarkus demo: DynamoDB Client

This example showcases how to use the AWS DynamoDB client with Quarkus. As a prerequisite install Install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# DynamoDB local instance

Just run it as follows:
`docker run --rm --name local-dynamo -p 8000:4569 -e SERVICES=dynamodb -e START_WEB=0 -d localstack/localstack`

DynamoDB listens on `localhost:8000` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Create table

Create a DynamoDB table using AWS CLI and the localstack profile.
```
aws dynamodb create-table --table-name QuarkusFruits \
                          --attribute-definitions AttributeName=fruitName,AttributeType=S \
                          --key-schema AttributeName=fruitName,KeyType=HASH \
                          --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
                          --profile localstack --endpoint-url=http://localhost:8000
```

# Run the demo on dev mode

- Run `./mvnw clean package` and then `java -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev`

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html), it should show a simple App to manage a list of Fruits. 
You can add fruits to the list via the form.

Alternatively, go to [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html) with the simple App communicating with Async resources.

# Running in native

You can compile the application into a native executable using:

`./mvnw clean install -Pnative`

and run with:

`./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner` 


# Running native in container

Build a native image in container by running:

`./mvnw package -Pnative -Dnative-image.docker-build=true`

Build a docker image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-dynamodb-quickstart .`

Create a network that connects your container with localstack
`docker network create localstack`

Stop your localstack container you started at the beginning
`docker stop local-dynamo`

Start localstack and connect to the network
`docker run --rm --network=localstack --name localstack -p 8000:4569 -e SERVICES=dynamodb -e START_WEB=0 -d localstack/localstack`

Create Dynamo table
```
aws dynamodb create-table --table-name QuarkusFruits \
                          --attribute-definitions AttributeName=fruitName,AttributeType=S \
                          --key-schema AttributeName=fruitName,KeyType=HASH \
                          --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
                          --profile localstack --endpoint-url=http://localhost:8000
```

Run quickstart container connected to that network (note that we're using internal port of the localstack)
`docker run -i --rm --network=localstack -p 8080:8080 quarkus/amazon-dynamodb-quickstart -Dquarkus.dynamodb.endpoint-override=http://localstack:4569`

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html) or [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html)

# Using AWS account

Before you can use the AWS SDKs with DynamoDB, you must get an AWS access key ID and secret access key. 
For more information, see [Setting Up DynamoDB (Web Service)](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SettingUp.DynamoWebService.html).

Create a DynamoDB table using AWS CLI and your default AWS profile:

```
aws dynamodb create-table --table-name QuarkusFruits \
                          --attribute-definitions AttributeName=fruitName,AttributeType=S \
                          --key-schema AttributeName=fruitName,KeyType=HASH \
                          --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

## Run demo

You can run the demo the same way as for a local instance, but you need to change the `application.properties`.

- remove or comment out `quarkus.dynamodb.endpoint-override` - as you are going to communicate with the AWS service now
- remove or comment out `quarkus.dynamodb.aws.region` - region is going to be retrieved via the default providers chain in the following order:
    - `aws.region` system property
    - `region` property from the profile file
- remove or comment out `quarkus.dynamodb.aws.credentials.type` - if not configured the client uses `default` credentials provider chain that looks for credentials in this order:
    - Java System Properties - `aws.accessKeyId` and `aws.secretKey`
    - Environment Variables - `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
    - Credential profiles file at the default location (`~/.aws/credentials`) shared by all AWS SDKs and the AWS CLI
    
Build the application

`./mvnw clean package`
 
And then run it

`java -jar ./target/quarkus-app/quarkus-run.jar`

Or, build as native executable

`./mvnw clean package -Pnative` 

And then run it

`./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner` 
