FROM gcr.io/cloud-builders/mvn as builder
COPY . /project
WORKDIR /project
RUN mvn -Duser.home=/builder/home -B install

FROM quay.io/quarkus/centos-quarkus-native-image:graalvm-1.0.0-rc15 as nativebuilder
COPY --from=builder /project/target /project/
WORKDIR /project
RUN  /opt/graalvm/bin/native-image -J-Djava.util.logging.manager=org.jboss.logmanager.LogManager \
     -J-Dcom.sun.xml.internal.bind.v2.bytecode.ClassTailor.noOptimize=true \
     -H:InitialCollectionPolicy='com.oracle.svm.core.genscavenge.CollectionPolicy$BySpaceAndTime' \
     -jar quarkus-quickstart-knative-runner.jar -J-Djava.util.concurrent.ForkJoinPool.common.parallelism=1 \
     -H:+PrintAnalysisCallTree -H:EnableURLProtocols=http \
     -H:-SpawnIsolates -H:-JNI --no-server -H:-UseServiceLoaderFeature -H:+StackTrace \
     && cp  -v quarkus-quickstart-knative-runner /tmp/quarkus-knative-runner

FROM registry.fedoraproject.org/fedora-minimal as lddpackage
RUN mkdir -p /work
COPY --from=nativebuilder /tmp/quarkus-knative-runner /work/application
COPY ./copy_ldd.sh /work
RUN chmod -R 775 /work && cd work && ./copy_ldd.sh application build

FROM scratch
COPY --from=lddpackage /work/build /
ENTRYPOINT ["/app/application","-Dquarkus.http.host=0.0.0.0"]
