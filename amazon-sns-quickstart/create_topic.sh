#!/usr/bin/env bash

export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8009
export PROFILE="localstack"
export TOPIC="QuarksCollider"


$(which aws) sns create-topic --name=${TOPIC} --profile ${PROFILE} --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT} | \
            jq -r .TopicArn
