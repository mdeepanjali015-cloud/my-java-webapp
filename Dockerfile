FROM openjdk:11-jre-slim
VOLUME /tmp
COPY target/my-java-webapp.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]