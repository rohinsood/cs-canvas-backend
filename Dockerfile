# syntax=docker/dockerfile:1
FROM openjdk:18-alpine3.13
WORKDIR /
RUN apk update && apk upgrade && \
    apk add --no-cache git 
COPY . /
RUN ./mvnw package
CMD ["java", "-jar", "target/spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8085