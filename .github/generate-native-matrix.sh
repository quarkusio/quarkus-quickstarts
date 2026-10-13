#! /bin/bash

IFS=$'\n'
# Exclusions from native matrix:
# neo4j: extension not compatible with Quarkus 4 yet
# optaplanner: discontinued, see https://github.com/apache/incubator-kie-optaplanner/pull/3177
# amazon-*: quarkus-amazon-services not compatible with Quarkus 4 yet (quarkiverse/quarkus-amazon-services#2253)
# kafka-avro-schema: Apicurio Registry not compatible with Vert.x 5 yet (quarkusio/quarkus-quickstarts#1691)
# security-jpa-reactive: native BootstrapMethodError in generated code (quarkusio/quarkus#57368)
modules=($(grep -LR --include=pom.xml '<packaging>pom</packaging>' */ | xargs dirname | grep -v neo4j | grep -v optaplanner | grep -v '^amazon-' | grep -v kafka-avro-schema | grep -v security-jpa-reactive | sort))
count=${#modules[@]}
partition=$(expr $count / 4 + 1)

json=$'{\n'
json+=$'    "include": [\n'
json+=$'    {\n'
i=0
partition_index=1
for module in ${modules[@]}; do
    if [ $i -gt $partition ]; then
        json+=$'"\n    },\n    {\n'
        i=0
    fi
    if [ $i -eq 0 ]; then
        json+="        \"category\": \"Native Tests - $partition_index\","
        json+=$'\n        "test-modules": "'
        ((partition_index=partition_index+1))
        i=0
    fi
    json+="$module,"
    ((i=i+1))
done
json+=$'"\n    }\n    ]\n}\n'

echo "$json"
