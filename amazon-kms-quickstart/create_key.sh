#!/usr/bin/env bash

export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8011
export PROFILE="localstack"
export KEY_SPEC="AES_256"

# Create KMS Key and assign it a value
export AWS_KEY_ID=`$(which aws) kms create-key --profile ${PROFILE} --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT} | \
              $(which jq) -r .KeyMetadata.KeyId`


# use it to generate data key
$(which aws) kms generate-data-key --key-id ${AWS_KEY_ID} --key-spec ${KEY_SPEC} --profile ${PROFILE} \
                --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT} > /dev/null

echo ${AWS_KEY_ID}
