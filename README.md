[![Actions Status](https://github.com/VictorGotsenko/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/VictorGotsenko/java-project-99/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=VictorGotsenko_java-project-99&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=VictorGotsenko_java-project-99)

## Task manager

This is an application that allows you to track progress towards your goals through task management, like [Redmine](http://www.redmine.org) .

Allows you to create entities such as: password-protected users, tasks, labels and task tags. You can also filter by these parameters.

### See it work
The UI version is available 🌐[here](https://java-project-99-5u14.onrender.com) , use it to log in:
```bash
username: hexlet@example.com
password: qwerty
```
It can be used as a server application without a user interface (backend application).
 - The API can be found on the [page](https://java-project-99-5u14.onrender.com/swagger-ui/index.html)

### How to use
- System requirements: Gradle 8.5 and Java ver.21 

Clone the project locally and run:

```bash
# run application in development mode
make run

# go to http://localhost:8080
# use username: hexlet@example.com
# password: qwerty
```

#### Used technologies:
 - Backend: Java, Spring Boot,SpringDoc WebMVC UI, LOMBOK, Mapstruct, HikariCP, PostgreSQL, H2database, 
 - Tests: JUnit, MockWebServer, Instacio, Javacrumbs
 - Frontend: Vite, React, TS
