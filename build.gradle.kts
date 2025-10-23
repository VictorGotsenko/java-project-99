plugins {
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "6.2.0.5505"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"
description = "Project for Spring Boot"

application {
    mainClass = "hexlet.code.AppApplication"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
//val h2databaseVer = "2.3.232"
val HikariCPVer = "6.3.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("net.datafaker:datafaker:2.4.4")

    // модуль jackson-databind-nullable
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")




    // конфиг  Mapstruct не работает с классами, в которых используется lombok
    // https://ru.stackoverflow.com/questions/1286369
    compileOnly("org.projectlombok:lombok:1.18.38")
    compileOnly("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok:1.18.38")


    //    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.h2database:h2:2.3.232")
//    implementation("com.h2database:h2:$h2databaseVer") // database H2 & HikariCP
    implementation("com.zaxxer:HikariCP:$HikariCPVer")
    // for  driver class: org.postgresql.Driver
    implementation("org.postgresql:postgresql:42.7.7")


    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //**** for Test Render
    testImplementation(platform("org.junit:junit-bom:6.0.0-M1"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
    //****

    // test JSON struct
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
    // Instacio
    testImplementation("org.instancio:instancio-junit:5.5.1")

}

checkstyle {
    toolVersion = "10.26.1"
    configFile = file("config/checkstyle/checkstyle.xml")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.withType<JavaCompile>(){
    // "--warning-mode all",
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
