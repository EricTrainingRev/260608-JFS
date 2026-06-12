**File 2: `tech.md`**
```md
# Tech Stack

## Backend

* **Language**: Java 21
* **Framework**: Spring Boot 4.1.0
* **Build System**: Gradle 9.5.1 (Kotlin DSL, `build.gradle.kts`)
* **Database**: SQLite (file: `todo.db`) via `sqlite-jdbc 3.53.2.0`
* **ORM**: Spring Data JPA with `hibernate-community-dialects` (SQLiteDialect)
* **Auth**: JWT via `jjwt` (api `0.13.0`, impl + jackson as runtime)
* **Boilerplate reduction**: Lombok

## Frontend

* **Framework**: Angular 
* **Routing**: Angular Router (Guarded with CanActivate for auth)
* **State**: RxJS Observables / LocalStorage (JWT storage)

## Testing

* **Unit/Integration**: JUnit 5 via Spring Boot test starters
* **In-memory DB**: H2 `2.4.240` (replaces SQLite during automated testing)

## Key Configuration (`application.properties`)

```properties
spring.datasource.url=jdbc:sqlite:todo.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true