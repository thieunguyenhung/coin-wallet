# COIN WALLET
This project is an example of exploring Kotlin with Spring Boot for educational and learning purposes. Please note that this is not intended for production use and should not be considered as an official endorsement or recommendation of Kotlin or Spring Boot.

## Features

- REST Api for POST request with body
- Validating request body with spring-boot-starter-validation and custom ConstraintValidator
- Handle exception with ExceptionHandler for RestControllerAdvice
- Configuring the persistence layer with JpaRepository
- Integration tests for Controller and Repository

## How to run locally

1. Run `docker-compose up -d`
2. Run application
    - **IntelliJ**: Edit Run configuration in Vm options add line `-Dspring.profiles.active=local`
    - **Gradle**: Run `./gradlew bootRun --args='--spring.profiles.active=dev'`

## Testing

1. Run `./gradlew jacocoTestReport`
2. Report can be found in "${buildDir}/JacocoReport/test/html", open `index.html` in browser to view.

## Disclaimer
Do what ever you want, I do not accept any responsibility or liability for any errors, omissions, or damages arising from the use of this example project.
