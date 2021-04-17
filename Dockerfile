FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
#RUN mvn install 
RUN ["mvn", "package", "-Dmaven.test.skip=true"]
#RUN mvn package

#FROM openjdk:11-jre-alpine
FROM adoptopenjdk/openjdk11:jre-11.0.10_9-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/oteldemo-0.0.1-SNAPSHOT.jar /app/

COPY opentelemetry-javaagent-all.jar opentelemetry-javaagent-all.jar

ENTRYPOINT ["java", "-javaagent:opentelemetry-javaagent-all.jar", "-jar", "oteldemo-0.0.1-SNAPSHOT.jar"]