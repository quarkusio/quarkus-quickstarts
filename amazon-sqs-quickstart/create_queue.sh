#!/usr/bin/env bash

export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8010
export PROFILE="localstack"
export QUEUE="ColliderQueue"


$(which aws) sqs create-queue --queue-name=${QUEUE} --profile ${PROFILE} --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT} | \
            jq -r .QueueUrl
