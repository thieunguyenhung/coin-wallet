# COIN WALLET

![Build Gradle](https://github.com/thieunguyenhung/coin-wallet/actions/workflows/build-gradle.yml/badge.svg)

This project is an example of exploring Kotlin with Spring Boot for educational and learning purposes. Please note that this is not intended for production use and should not be considered as an official endorsement or recommendation of Kotlin or Spring Boot.

## Features

- REST Api for POST request with body
- API Documentation with `spring-restdocs-restassured` and `com.epages:restdocs-api-spec-gradle-plugin`
- Validating request body with `spring-boot-starter-validation` and custom ConstraintValidator
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
2. Open `${buildDir}/JacocoReport/test/html/index.html` in browser to view

## API Documentation

1. Run `./gradlew openapi3`
2. Open `${buildDir}/api-spec/openapi3.yaml` in Swagger UI or any yaml reader for API details

## Disclaimer
Do what ever you want, I do not accept any responsibility or liability for any errors, omissions, or damages arising from the use of this example project.
