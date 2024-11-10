#!/bin/bash
# Generate a KMS key
key_id=$(awslocal kms create-key --query KeyMetadata.KeyId --output text)
# Set an alias for the KMS key
awslocal kms create-alias --alias-name "alias/quarkus" --target-key-id $key_id
