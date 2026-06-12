# Tech Stack

## Language & Runtime

- Java 21 (toolchain-enforced)
- Lombok for boilerplate reduction (`@Data`, `@Builder`, etc.)

## Framework

- Spring Boot 4.1.0
- Spring Web MVC (REST controllers)
- Spring Data JPA (repository pattern)

## Database

- SQLite (production, file: `todo.db` at project root)
- Hibernate Community Dialect for SQLite (`org.hibernate.community.dialect.SQLiteDialect`)
- H2 (in-memory, used for tests)
- DDL strategy: `create-drop` (tables rebuilt on each startup — development mode)

## Authentication

- JJWT 0.13.0 (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`)

## Build System

- Gradle (Kotlin DSL)
- Spring Dependency Management plugin 1.1.7

## Testing

- JUnit 5 (JUnit Platform)
- Spring Boot Test (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`)

## Common Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

## Conventions

- Use Lombok annotations to reduce boilerplate — avoid writing getters/setters/constructors manually.
- Keep `application.properties` as the single configuration source (no YAML).
    - Use a "test.properties" to manage test configuration
- SQL logging is enabled (`show-sql=true`, formatted) for development visibility.
