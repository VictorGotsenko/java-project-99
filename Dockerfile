FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

ENV SPRING_PROFILES_ACTIVE=dev

RUN ./gradlew --no-daemon clean build --info

CMD ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]
