FROM openjdk:17
WORKDIR /app
COPY forum-oss/target/forum-oss-0.0.1-SNAPSHOT.jar /app/spring.jar
ENTRYPOINT ["java", "-jar", "/app/spring.jar", "--spring.config.location=/app/application.yaml", "--logging.config=/app/logback-spring.xml"]