.DEFAULT_GOAL := build-run

define SPRING_PROFILES_ACTIVE
dev
endef

clean:
	./gradlew clean
lint:
	./gradlew checkstyleMain checkstyleTest
build:
	./gradlew clean build
run:
	./gradlew run
test:
	./gradlew test
report:
	./gradlew jacocoTestReport

build-run: build run
