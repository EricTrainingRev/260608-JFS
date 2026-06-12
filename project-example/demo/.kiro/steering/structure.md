# Project Structure

```
demo/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew / gradlew.bat
├── todo.db
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   └── exception/
│   │   └── resources/
│   └── test/
│       └── java/com/example/demo/
│           ├── controller/
│           ├── service/
│           └── repository/
└── .kiro/
    └── steering/
```

## Rules

- One class per file; filename matches class name.
- Place new classes in the appropriate sub-package — do not put everything in the root `demo` package.
- Tests mirror the main source structure: `src/test/java/com/example/demo/{layer}/`.
- Keep the SQLite DB file (`todo.db`) at project root; do not commit it to version control.
