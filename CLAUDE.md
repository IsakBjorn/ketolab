# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
./mvnw compile              # Compile the project
./mvnw test                 # Run all tests
./mvnw test -Dtest=TestClassName           # Run a single test class
./mvnw test -Dtest=TestClassName#testName  # Run a single test method
./mvnw spring-boot:run      # Run the application locally
./mvnw clean package        # Build JAR artifact
```

## Tech Stack

- **Language:** Kotlin 2.2.21 with Java 25
- **Framework:** Spring Boot 4.0.2 with WebMVC
- **Build:** Maven with wrapper (3.9.12)
- **Testing:** JUnit 5 with kotlin-test-junit5

## Architecture

This is a Spring Boot web application skeleton following standard conventions:

- **Entry point:** `KetolabApplication.kt` uses `@SpringBootApplication`
- **Package structure:** `no.ibear.ketolab`
- **Configuration:** `application.properties` in resources
- **Templates/Static:** Standard Spring Boot directories under resources

## Kotlin Configuration

The project uses strict null-safety (`-Xjsr305=strict`) and the Spring compiler plugin for AOP support. Constructor injection works automatically with Kotlin's `param-property` annotation target.
