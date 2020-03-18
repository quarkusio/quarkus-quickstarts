# Quarkus demo: DynamoDB Client

This example showcases how to use the DynamoDB client with Quarkus. 

# DynamoDB instance
## Option 1 - Running with Docker

Just run it as follows:
`docker run -it -p 8000:8000 amazon/dynamodb-local:1.11.477 -jar DynamoDBLocal.jar -inMemory -sharedDb`

DynamoDB listens on `localhost:8000` for REST endpoints. A shell is available at `http://localhost:8000/shell`.

**Provision table**

Open `http://localhost:8000/shell` in your browser.

Copy & paste a following code to the shell and run it.
```js
var params = {
    TableName: 'QuarkusFruits',
    KeySchema: [{ AttributeName: 'fruitName', KeyType: 'HASH' }],
    AttributeDefinitions: [{  AttributeName: 'fruitName', AttributeType: 'S', }],
    ProvisionedThroughput: { ReadCapacityUnits: 1, WriteCapacityUnits: 1, }
};

dynamodb.createTable(params, function(err, data) {
    if (err) ppJson(err);
    else ppJson(data);

});
```

## Option 2 - Use DynamoDB on your AWS account

Before you can use the AWS SDKs with DynamoDB, you must get an AWS access key ID and secret access key. 
For more information, see [Setting Up DynamoDB (Web Service)](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/SettingUp.DynamoWebService.html).

**Provision table with AWS CLI**

```
aws dynamodb create-table --table-name QuarkusFruits \
                          --attribute-definitions AttributeName=fruitName,AttributeType=S \
                          --key-schema AttributeName=fruitName,KeyType=HASH \
                          --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

**Provision table with AWS Web Console**

1. Open console at https://console.aws.amazon.com/dynamodb/
2. Choose **Create Table**
3. In the **Create DynamoDB table** screen, do the following:
    - In the **Table name** box, enter **QuarkusFruits**.
    - For the **Primary key**, do the following:
      - In the **Partition key** box, enter **fruitName**. Set the data type to **String**.
4. When the settings are as you want them, choose **Create**.


# Run the demo on dev mode

- Run `mvn clean package` and then `java -jar ./target/dynamodb-quickstart-1.0-SNAPSHOT-runner.jar`
- In dev mode `mvn clean quarkus:dev`

Go to `http://localhost:8080/fruits.html`, it should show a simple App to manage list of Fruits. 
You can add fruits to the list via the form.

Alternatively, go to `http://localhost:8080/async-fruits.html` with the simple App communicating with Async resources.

# Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/dynamodb-quickstart-1.0-SNAPSHOT-runner` 


# Running native in container
--
Build a native image in container by running:

`mvn install -Pnative -Dnative-image.docker-build=true`

Build a docker image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/dynamodb-quickstart .`

And run it with:
`docker run -i --rm -p 8081:8081 quarkus/dynamodb-client`

Go to `http://localhost:8081/fruits.html` or `http://localhost:8081/async-fruits.html`


