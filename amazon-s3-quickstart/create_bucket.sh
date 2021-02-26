#!/usr/bin/env bash

export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8008
export PROFILE="localstack"
export BUCKET="quarkus.s3.quickstart"


$(which aws) s3 mb s3://${BUCKET} --profile ${PROFILE} --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT}

