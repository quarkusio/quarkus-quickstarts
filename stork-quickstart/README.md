Quarkus guide: https://quarkus.io/guides/stork

You can start the Consul instance using:


```shell
docker run \
    -d \
    -p 8500:8500 \
    -p 8600:8600/udp \
    consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
```
