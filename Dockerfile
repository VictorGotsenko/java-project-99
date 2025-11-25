FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

ENV SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN}

RUN ./gradlew --no-daemon clean build

CMD ["java", "-jar", "build/libs/app-0.0.1-SNAPSHOT.jar"]
