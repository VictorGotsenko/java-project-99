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

    //    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.h2database:h2:2.3.232")
//    implementation("com.h2database:h2:$h2databaseVer") // database H2 & HikariCP
    implementation("com.zaxxer:HikariCP:$HikariCPVer")
    // for  driver class: org.postgresql.Driver
    implementation("org.postgresql:postgresql:42.7.7")



    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

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

tasks.withType<Test> {
    useJUnitPlatform()
}
