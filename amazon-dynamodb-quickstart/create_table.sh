#!/usr/bin/env bash


export ATTRIBUTE_NAME="fruitName"
export ATTRIBUTE_TYPE="S"
export CAPACITY_UNITS_READ=1
export CAPACITY_UNITS_WRITE=1
export ENDPOINT_HOST="localhost"
export ENDPOINT_PORT=8000
export KEY_TYPE="HASH"
export PROFILE="localstack"
export TABLE_NAME="QuarkusFruits"

$(which aws) dynamodb create-table --table-name ${TABLE_NAME} \
                          --attribute-definitions AttributeName=${ATTRIBUTE_NAME},AttributeType=${ATTRIBUTE_TYPE} \
                          --key-schema AttributeName=${ATTRIBUTE_NAME},KeyType=${KEY_TYPE} \
                          --provisioned-throughput ReadCapacityUnits=${CAPACITY_UNITS_READ},WriteCapacityUnits=${CAPACITY_UNITS_WRITE} \
                          --profile ${PROFILE} --endpoint-url=http://${ENDPOINT_HOST}:${ENDPOINT_PORT} | \
                          grep -E 'TableName|TableStatus' | xargs


