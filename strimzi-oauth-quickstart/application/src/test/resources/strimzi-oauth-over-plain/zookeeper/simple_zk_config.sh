#!/bin/bash

declare -A props

to_property_name() {
  key=$1
  echo ${key:3}
}

pop_value() {
  key=$1
  fallback=$2

  if [ -z ${props[$key]+x} ] ; then
    echo $fallback
  else
    echo ${props[$key]}
  fi
  unset props[$key]
}

unset IFS
for var in $(compgen -e); do
  if [[ $var == ZK_* ]]; then
    props[`to_property_name $var`]=${!var}
  fi
done

#
# Generate output
#
echo "#"
echo "# strimzi.properties"
echo "#"

echo dataDir=`pop_value dataDir /tmp/zookeeper`
echo clientPort=`pop_value clientPort 2181`
echo maxClientCnxns=`pop_value maxClientCnxns 0`

#
# Add what remains of ZK_* env vars
#
for K in "${!props[@]}"
do
  echo $K=`pop_value $K`
done

echo