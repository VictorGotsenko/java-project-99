.DEFAULT_GOAL := build-run

clean:
	./gradlew clean
lint:
	./gradlew checkstyleMain checkstyleTest
build:
	./gradlew clean build
run:
	./gradlew run --args='--spring.profiles.active=dev'	
test:
	./gradlew test
report:
	./gradlew jacocoTestReport	

build-run: build run
