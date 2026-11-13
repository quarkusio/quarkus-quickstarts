#!/bin/bash
aws dynamodb create-table \
    --table-name QuarkusFruits \
    --attribute-definitions \
        AttributeName=partitionKey,AttributeType=S \
        AttributeName=fruitName,AttributeType=S \
    --key-schema \
        AttributeName=partitionKey,KeyType=HASH \
        AttributeName=fruitName,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
