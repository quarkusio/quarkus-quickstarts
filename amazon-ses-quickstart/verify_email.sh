#!/usr/bin/env bash

export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8012
export PROFILE="localstack"


for VAR in "$@"
do
  $(which aws) ses verify-email-identity --email-address ${VAR} --profile ${PROFILE} \
      --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT}
  echo "Verified " ${VAR}
done
