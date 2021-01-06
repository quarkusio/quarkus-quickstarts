#!/bin/bash

./simple_zk_config.sh | tee /tmp/strimzi.properties

exec /opt/kafka/bin/zookeeper-server-start.sh /tmp/strimzi.properties