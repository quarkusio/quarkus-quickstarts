####
# Before building the docker image run:
#
# mvn package -Pnative -Dnative-image.docker-build=true
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile -t shamrock/using-opentracing .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 shamrock/using-opentracing
#
###
FROM registry.fedoraproject.org/fedora-minimal
WORKDIR /work/
COPY target/*-runner /work/application
RUN chmod 775 /work
EXPOSE 8080
CMD ["./application", "-Dshamrock.http.host=0.0.0.0"]