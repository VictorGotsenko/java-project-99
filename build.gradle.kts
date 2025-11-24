plugins {
    application
    checkstyle
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("org.springframework.boot") version "3.5.6"
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.0.1.6134"
    id("io.sentry.jvm.gradle") version "5.12.2"
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
    maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-devtools")
    //
    implementation("net.datafaker:datafaker:2.5.3")
    // SpringDoc OpenAPI Starter WebMVC UI » 2.8.13
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    // модуль jackson-databind-nullable
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    // Mapstruct
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    // конфиг  Mapstruct не работает с классами, в которых используется lombok
    // https://ru.stackoverflow.com/questions/1286369
    compileOnly("org.projectlombok:lombok:1.18.38")
    compileOnly("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    // DataBase section
    implementation("com.zaxxer:HikariCP:6.3.0")
    runtimeOnly("com.h2database:h2")
    implementation("com.h2database:h2:") // database H2 & HikariCP
    // for  driver class: org.postgresql.Driver
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.postgresql:postgresql:42.7.7")
    // *** Tests ***
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    //
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // стартёр тестов
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // для аутентификации
    testImplementation("org.springframework.security:spring-security-test")
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

tasks.withType<JavaCompile>() {
    // "--warning-mode all",
    options.compilerArgs.addAll(listOf("-Xlint:deprecation", "-Xlint:unchecked"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sentry {
    // Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
    // This enables source context, allowing you to see your source
    // code as part of your stack traces in Sentry.
    includeSourceContext = true

    org = "sprojects-fj"
    projectName = "java-project-99"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
    enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

sonar {
    properties {
        property("sonar.projectKey", "VictorGotsenko_java-project-99")
        property("sonar.organization", "victorgotsenko")
        property("sonar.host.url", "https://sonarcloud.io")
        // property("sonar.log.level", "TRACE")
        // Отключаем проверку зависимостей
        property("sonar.dependencyVerification.enabled", "false")
    }
}


