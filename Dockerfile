FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN ./gradlew --no-daemon clean installDist --info

CMD ./build/install/app/bin/app
