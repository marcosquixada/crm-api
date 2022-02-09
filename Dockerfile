FROM adoptopenjdk/openjdk11:alpine
ARG PORT=8080
ARG JAR_FILE=target/*.jar
VOLUME [ "/data" ]
EXPOSE ${PORT}
COPY ${JAR_FILE} /api/application/api.jar
WORKDIR /api/application
ENTRYPOINT [ "java", "-jar", "api.jar" ]