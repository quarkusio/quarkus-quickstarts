Quarkus guide: https://quarkus.io/guides/stork

You can start the Consul instance using:


```shell
docker run --rm --name consul -p 8500:8500 -p 8501:8501 -p 8600:8600/udp consul:1.9 agent -dev -ui -client=0.0.0.0 -bind=0.0.0.0 --https-port=8501
```

Then start the app 

```shell
mvn quarkus:dev
```

Then try to go to http://localhost:8080/api and refresh several times. You should see `Hello from Red!` and `Hello from Blue!` alternatively.

