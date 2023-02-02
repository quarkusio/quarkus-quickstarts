VERSION=2.13.0.Final

echo "Generating Quarkus template to fetch the updated Dockerfile resources"
TEMPLATE_FOLDER=$(mktemp -d)
TEMPLATE_NAME=REPLACE_WITH_QUICKSTART_NAME
pushd $TEMPLATE_FOLDER
# Using "io.quarkus:quarkus-maven-plugin" to work with either 999-SNAPSHOT and a released version
mvn io.quarkus:quarkus-maven-plugin:${VERSION}:create -DprojectGroupId=org.acme -DprojectArtifactId=$TEMPLATE_NAME
popd
DOCKERFILES=(Dockerfile.jvm Dockerfile.legacy-jar Dockerfile.native Dockerfile.native-micro)
# List of quickstarts that should not update the Dockerfile resources (list separated by comma)
EXCLUDE_DOCKERFILE_UPDATE_FOR_QUICKSTARTS="awt-graphics-rest-quickstart"

for quickstart in *-quickstart; do

  # Update Dockerfile files:
  if [[ $EXCLUDE_DOCKERFILE_UPDATE_FOR_QUICKSTARTS != *"$quickstart"* ]]; then
    echo "Updating ${quickstart}"
  else
    echo "NOT Updating ${quickstart}"
  fi
done

echo "Deleting generated Quarkus template"
rm -rf $TEMPLATE_FOLDER