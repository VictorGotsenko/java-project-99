FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN ["./gradlew", "clean", "build"]

CMD ["java", "-jar", "build/libs/java-project-99-0.0.1-SNAPSHOT.jar"]
