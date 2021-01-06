#!/bin/bash
set -e

source functions.sh

URI=${KEYCLOAK_URI}
if [ "" == "${URI}" ]; then
    URI="http://${KEYCLOAK_HOST:-keycloak}:8080/auth"
fi

wait_for_url $URI "Waiting for Keycloak to start"

wait_for_url "$URI/realms/${REALM:-demo}" "Waiting for realm '${REALM}' to be available"

if [ "$SERVER_PROPERTIES_FILE" == "" ]; then
  echo "Generating a new strimzi.properties file using ENV vars"
  ./simple_kafka_config.sh | tee /tmp/strimzi.properties
else
  echo "Using provided server.properties file: $SERVER_PROPERTIES_FILE"
  cp $SERVER_PROPERTIES_FILE /tmp/strimzi.properties
fi

# add Strimzi kafka-oauth-* jars and their dependencies to classpath
export CLASSPATH="/opt/kafka/libs/strimzi/*:$CLASSPATH"

exec /opt/kafka/bin/kafka-server-start.sh /tmp/strimzi.properties