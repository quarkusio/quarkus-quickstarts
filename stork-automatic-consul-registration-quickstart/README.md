Quarkus guide: https://quarkus.io/guides/stork-registration

This example describes how Stork can registers automatically service instances using Consul.

You can start the Consul instance using:


```shell
docker run \
    -d \
    -p 8500:8500 \
    -p 8600:8600/udp \
    consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
```
Registering services in command line:

```bash
curl -X PUT -d '{"ID": "red", "Name": "red-service", "Address": "localhost", "Port": 9000, "Tags": ["color"]}' http://127.0.0.1:8500/v1/agent/service/register
```

Deleting a service instance

```bash
curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/red
```

Getting a service by name

```bash
 curl -X GET http://127.0.0.1:8500/v1/agent/service/red 
```
