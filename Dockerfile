FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN ./gradlew --no-daemon clean build

CMD ["java", "-jar", "java-project-99-0.0.1-SNAPSHOT-all.jar"]
