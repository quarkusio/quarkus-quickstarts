#!/bin/sh

set -e

STOREPASS=Z_pkTh9xgZovK4t34cGB2o6afT4zZg0L

echo "#### Generate broker keystore"
keytool -keystore cluster.keystore.p12 -alias localhost -validity 380 -genkey -keyalg RSA -ext SAN=DNS:kafka -dname "CN=my-cluster-kafka,O=io.strimzi" -deststoretype pkcs12 -storepass $STOREPASS -keypass $STOREPASS

echo "#### Add the CA to the brokers’ truststore"
keytool -keystore cluster.truststore.p12 -deststoretype pkcs12 -storepass $STOREPASS -alias CARoot -importcert -file ../../../certificates/ca.crt -noprompt

echo "#### Export the certificate from the keystore"
keytool -keystore cluster.keystore.p12 -storetype pkcs12 -alias localhost -certreq -file cert-file -storepass $STOREPASS

echo "#### Sign the certificate with the CA"
openssl x509 -req -CA ../../../certificates/ca.crt -CAkey ../../../certificates/ca.key -in cert-file -out cert-signed -days 400 -CAcreateserial -passin pass:$STOREPASS

echo "#### Import the CA and the signed certificate into the broker keystore"
keytool -keystore cluster.keystore.p12 -deststoretype pkcs12 -alias CARoot -import -file ../../../certificates/ca.crt -storepass $STOREPASS -noprompt
keytool -keystore cluster.keystore.p12 -deststoretype pkcs12 -alias localhost -import -file cert-signed -storepass $STOREPASS -noprompt
