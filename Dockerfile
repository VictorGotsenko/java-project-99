FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

ENV SPRING_PROFILES_ACTIVE=dev

RUN ./gradlew --no-daemon clean build

CMD ["java", "-jar", "build/libs/java-project-99-0.0.1-SNAPSHOT.jar"]
