# Gradle Quick Reference Guide

## Overview of Gradle
Gradle is a powerful build automation tool designed to handle the entire software lifecycle: compiling, testing, packaging, and deploying. While it supports many languages, it commonly used for Java and Android development.

**The "Engine" of Gradle: Automation & Flexibility**
Unlike older build tools that rely on rigid XML configurations, Gradle operates on a **Task-based Model** driven by a programmable build script. It is defined by three core pillars:
1.  **The Model (What):** Defining your dependencies and plugins.
2.  **The Logic (How):** Writing custom tasks and automation steps.
3.  **The Lifecycle (When):** Understanding when configuration happens versus when execution happens.

## The Gradle Lifecycle: Configuration vs. Execution
One of the most common mistakes in Gradle is placing logic in the wrong phase. Understanding the "Life of a Build" is critical to preventing unexpected behavior.

**The Two Phases:**
1.  **The Configuration Phase:** Gradle evaluates your `build.gradle` file. It builds a "Task Graph" (a map of every task and how they depend on each other). 
    *   *Warning:* Any code written directly in the script (not inside a task block) runs during this phase, **every single time** you run any command.
2.  **The Execution Phase:** Gradle looks at the task you actually requested (e.g., `gradle test`) and follows the graph to run only the necessary tasks.

```groovy
// BAD: This prints EVERY time you run ANY gradle command
println "Configuring the project..." 

task hello {
    // GOOD: This only prints when you run 'gradle hello'
    doLast {
        println "Executing the task!"
    }
}
```

## Dependency Management: The Supply Chain
A modern application is rarely built from scratch; it relies on a "supply chain" of external libraries. Gradle manages this through **Repositories** (where to look) and **Dependencies** (what to grab).

### 1. The Repository Strategy (Where to look)
Before Gradle can download a library, it needs to know which "warehouse" to check.
*   `mavenCentral()`: The industry-standard public repository.
*   `google()`: Essential for Android development.
*   `ivy()` or `mavenLocal()`: Used for private or local development repositories.

### 2. Dependency Scopes (The "How Much" and "When")
Not all libraries are needed at all times. Using the correct **scope** keeps your final application lightweight and secure.

| Scope | Usage | Visibility |
| :--- | :--- | :--- |
| **`implementation`** | The standard for internal dependencies. | Hidden from consumers of your library (prevents "dependency bloat"). |
| **`api`** | Used when building libraries. | Exposed to anyone who uses your library (transitive). |
| **`testImplementation`** | For testing frameworks (e.g., JUnit). | Only available during the `test` task. |
| **`runtimeOnly`** | For things needed only when the app is running. | Not available during compilation. |
| **`compileOnly`** | For "provided" dependencies (e.g., Annotations). | Not included in the final package. |

## Plugins: The Functional Extensions
Plugins are the "modules" of Gradle. The core Gradle tool is actually quite minimal; almost all its power (like the ability to compile Java) comes from plugins.

**The Anatomy of a Plugin Application:**
```groovy
plugins {
    id 'java'          // Adds Java compilation, testing, and JAR packaging
    id 'application'   // Adds the ability to run the project as an app
}

application {
    mainClass = 'com.example.Main' // Configuration for the 'application' plugin
}
```

## Tasks: The Instruction Set
If Plugins provide the capabilities, **Tasks** are the individual units of work. A task might be "compile this file," "copy this folder," or "send a Slack notification."

### 1. Built-in Tasks (Standard Workflow)
*   `gradle clean` — Deletes the `build/` directory (starts fresh).
*   `gradle build` — Compiles, tests, and packages the project.
*   `gradle test` — Runs the test suite.

### 2. Custom Tasks (The Developer's Logic)
When the built-in tools aren't enough, you define your own. Always use `doLast` or `doFirst` to ensure your logic is part of the **Execution Phase**.

```groovy
task generateReport {
    description = 'Generates a custom build report.'
    group = 'Reporting' // Places the task in a specific category in './gradle tasks'

    doLast {
        println "Generating report..."
        // Logic goes here
    }
}
```

## The Professional Standard: The Gradle Wrapper
In a professional environment, you should **never** rely on a version of Gradle installed on your local machine. This leads to the "It works on my machine" syndrome.

**The Solution: `./gradlew`**
The Gradle Wrapper is a small set of files included in your project repository that automatically downloads and uses the **exact same version** of Gradle for every developer and every CI/CD server.

| Command | Purpose |
| :--- | :--- |
| `./gradlew build` | Runs the build using the project-specific wrapper (macOS/Linux). |
| `gradlew.bat build` | Runs the build using the project-specific wrapper (Windows). |

**The "Golden Rule" of Gradle:**
> If a project has a `gradlew` file, **always** use it instead of your local `gradle` installation.

## Summary: The Developer's Mental Checklist

When working on a Gradle project, ask yourself:

1.  **Is the scope correct?** (Should this be `implementation` or `api`?)
2.  **Is the logic in the right phase?** (Am I using `doLast` to prevent running code during Configuration?)
3.  **Am I using the Wrapper?** (Am I typing `./gradlew` instead of `gradle`?)
4.  **Is the dependency available?** (Did I declare the correct `repository`?)