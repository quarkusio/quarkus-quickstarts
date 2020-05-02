
# VARIABLES

# # general
BUILD_TOOL := ./mvnw
NAME := quarkus-infinispan-embedded-quickstart
JAR_FILE := $(shell find target -name '*runner.jar' 2>/dev/null)
PROTOCOL := http
PORT := 8080
METRICS_PORT := 8080
DEBUG_PORT := 5005
HEAP_SIZE := 128m
META_SIZE := 64m

# # docker
DOCKER_IMAGE_NAME := microservices/$(NAME)
DOCKER_IMAGE_TAG := latest
DOCKER_IMAGE_PORT := $(PORT)

DOCKER_IMAGE_METRICS_PORT := $(METRICS_PORT)
DOCKER_IMAGE_DEBUG_PORT := $(DEBUG_PORT)
DOCKER_IMAGE_JMX_PORT := 3$(METRICS_PORT)
DOCKER_IMAGE_PORTS := -p $(DOCKER_IMAGE_PORT)\:$(DOCKER_IMAGE_PORT) -p $(DOCKER_IMAGE_METRICS_PORT)\:$(DOCKER_IMAGE_METRICS_PORT) -p $(DOCKER_IMAGE_DEBUG_PORT)\:$(DOCKER_IMAGE_DEBUG_PORT) -p $(DOCKER_IMAGE_JMX_PORT)\:$(DOCKER_IMAGE_JMX_PORT)
DOCKER_REGISTRY_HOST := localhost
DOCKER_REGISTRY_PORT := 5000

# # retrieving docker IP address on localhost
DOCKER_ENVVARS := -e QUARKUS_PROFILE=prod -e HEAP_SIZE=$(HEAP_SIZE) -e META_SIZE=$(META_SIZE)

# # jvm
MEM_OPTS := -Xms$(HEAP_SIZE) -Xmx$(HEAP_SIZE) -XX:MaxMetaspaceSize=$(META_SIZE)
JMX_OPTS := -Dcom.sun.management.jmxremote.port=$(DOCKER_IMAGE_JMX_PORT) -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false
DEBUG_OPTS := -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$(DOCKER_IMAGE_DEBUG_PORT)
OTHER_OPTS := -Dquarkus.profile=prod
# # makefile
.PHONY: help
.DEFAULT_GOAL := help

# FUNCTIONS

# # building

dependencies :		## Show dependency tree
	$(BUILD_TOOL) dependency:tree -Dverbose

clean :		## Clean the application
	$(BUILD_TOOL) clean

compile :		## Compile the application
	$(BUILD_TOOL) compile

package :		## Build the application package including unit tests only
	$(BUILD_TOOL) package


# # testing
test :		## Run all tests
	$(BUILD_TOOL) test

testOnly :		## Run all tests
	$(BUILD_TOOL) surefire:test

#integration-test :		## compiles and run Integration tests
#	$(BUILD_TOOL) -Dtest=*IntegrationTest test


# # running

devRun :		## Run the application
	$(BUILD_TOOL) compile quarkus:dev # -DjvmArguments='$(MEM_OPTS) $(JMX_OPTS) $(OTHER_OPTS)'

javaRun :		## Run the application through the generated fat-jar
	java $(MEM_OPTS) $(OTHER_OPTS) -jar $(JAR_FILE)

javaDebug :		## Run the application in debug mode through the generated fat-jar
	java $(MEM_OPTS) $(OTHER_OPTS) $(DEBUG_OPTS) -jar $(JAR_FILE)

open :		## Open the browser to the application site
	open $(PROTOCOL)://localhost:$(PORT)

open-metrics :		## Open the browser to the application metrics page
	open $(PROTOCOL)://localhost:$(METRICS_PORT)


# # docker

dockerBuild :		## Build the docker image of the application
	docker build -f src/main/docker/Dockerfile.jvm -t $(DOCKER_IMAGE_NAME):$(DOCKER_IMAGE_TAG) .

dockerRun :		## Run the containerised application through docker
	docker run --rm -it --name $(NAME) $(DOCKER_ENVVARS) $(DOCKER_IMAGE_PORTS) $(DOCKER_IMAGE_NAME):$(DOCKER_IMAGE_TAG)

dockerRunDeamon :		## Run the containerised application as deamon through docker
	docker run --rm -d --name $(NAME) $(DOCKER_ENVVARS) $(DOCKER_IMAGE_PORTS) $(DOCKER_IMAGE_NAME):$(DOCKER_IMAGE_TAG)

docker-stop :		## Stop the containerised application
	docker container stop $(NAME)

docker-kill :		## Kill the containerised application
	docker container kill $(NAME)

docker-delete-local : docker-kill		## Delete the docker image of the application
	docker container rm -f $(NAME)
	docker image rm -f $(NAME)

docker-file-check :		## Verify Dockerfile with hadolint
	docker run --rm -i hadolint/hadolint < Dockerfile_local


# # general

help :		## Help
	@echo ""
	@echo "*** $(NAME) Makefile help ***"
	@echo ""
	@echo "Targets list:"
	@grep -E '^[a-zA-Z_-]+ :.*?## .*$$' $(MAKEFILE_LIST) | sort -k 1,1 | awk 'BEGIN {FS = ":.*?## "}; {printf "\t\033[36m%-30s\033[0m %s\n", $$1, $$2}'
	@echo ""

print-variables :		## Print variables values
	@echo "- - - makefile - - -"
	@echo "MAKE: $(MAKE)"
	@echo "MAKEFILES: $(MAKEFILES)"
	@echo "MAKEFILE_LIST: $(MAKEFILE_LIST)"
	@echo "- - - "
	@echo "- - - general - - -"
	@echo "BUILD_TOOL: $(BUILD_TOOL)"
	@echo "NAME: $(NAME)"
	@echo "JAR_FILE: $(JAR_FILE)"
	@echo "PROTOCOL: $(PROTOCOL)"
	@echo "PORT: $(PORT)"
	@echo "METRICS_PORT: $(METRICS_PORT)"
	@echo "DEBUG_PORT: $(DEBUG_PORT)"
	@echo "- - - "
	@echo "- - - docker - - -"
	@echo "DOCKER_IMAGE_NAME: $(DOCKER_IMAGE_NAME)"
	@echo "DOCKER_IMAGE_TAG: $(DOCKER_IMAGE_TAG)"
	@echo "DOCKER_IMAGE_PORT: $(DOCKER_IMAGE_PORT)"
	@echo "DOCKER_IMAGE_METRICS_PORT: $(DOCKER_IMAGE_METRICS_PORT)"
	@echo "DOCKER_IMAGE_DEBUG_PORT: $(DOCKER_IMAGE_DEBUG_PORT)"
	@echo "DOCKER_IMAGE_JMX_PORT: $(DOCKER_IMAGE_JMX_PORT)"
	@echo "DOCKER_DOCKER_IMAGE_PORTS: $(DOCKER_DOCKER_IMAGE_PORTS)"
	@echo "DOCKER_REGISTRY_HOST: $(DOCKER_REGISTRY_HOST)"
	@echo "DOCKER_REGISTRY_PORT: $(DOCKER_REGISTRY_PORT)"
	@echo "DOCKER_HOST_NAME: $(DOCKER_HOST_NAME)"
	@echo "DOCKER_HOST_IP: $(DOCKER_HOST_IP)"
	@echo "- - - "
	@echo "- - - "
	@echo "- - - jvm - - -"
	@echo "MEM_OPTS: $(MEM_OPTS)"
	@echo "JMX_OPTS: $(JMX_OPTS)"
	@echo "DEBUG_OPTS: $(DEBUG_OPTS)"
	@echo "OTHER_OPTS: $(OTHER_OPTS)"
	@echo "- - - "
	@echo ""


# LINKS

# Makefile manual	https://www.gnu.org/software/make/manual/make.html
