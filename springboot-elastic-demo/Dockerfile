FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/springboot-elastic-demo-0.0.7-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar","2223"]