FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN ./gradlew --no-daemon clean build

ENV JAVA_OPTS="-Xmx512M -Xms512M"

CMD ["java", "-jar", "build/libs/java-project-99-0.0.1-SNAPSHOT.jar"]
