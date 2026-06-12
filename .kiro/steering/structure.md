# Project Structure

## Repository Layout

```text
260608-JFS/
├── Phase 1/                        # Phase 1 deliverable docs & UI mockups
├── Phase_2/                        # Phase 2 deliverable docs, ERD, API contracts
├── Project1_TheBlind/
│   ├── project_1/                  # Legacy/scaffold project (deprecated)
│   └── todo-app/                   # Active project
│       ├── backend/                # Spring Boot application 
│       │   ├── src/
│       │   │   ├── main/
│       │   │   │   ├── java/com/theblind/todo/   # Application source
│       │   │   │   └── resources/
│       │   │   │       └── application.properties
│       │   │   └── test/
│       │   │       └── java/com/theblind/todo/   # Test source
│       │   ├── build.gradle.kts
│       │   ├── settings.gradle.kts
│       │   └── gradlew.bat
│       └── frontend/               # Angular application
└── .kiro/
    └── steering/                   # AI steering documents