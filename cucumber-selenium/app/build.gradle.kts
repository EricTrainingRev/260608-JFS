plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)

    // Source: https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
    implementation("org.seleniumhq.selenium:selenium-java:4.45.0")

// Source: https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.1")
// Source: https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
testImplementation("org.junit.jupiter:junit-jupiter-engine:6.1.1")
// Source: https://mvnrepository.com/artifact/org.junit.platform/junit-platform-suite-api
testImplementation("org.junit.platform:junit-platform-suite-api:6.1.1")
// Source: https://mvnrepository.com/artifact/org.junit.platform/junit-platform-suite-engine
testImplementation("org.junit.platform:junit-platform-suite-engine:6.1.1")

    // Cucumber
// Source: https://mvnrepository.com/artifact/io.cucumber/cucumber-java
implementation("io.cucumber:cucumber-java:7.34.4")
// Source: https://mvnrepository.com/artifact/io.cucumber/cucumber-junit-platform-engine
testImplementation("io.cucumber:cucumber-junit-platform-engine:7.34.4")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.test {
    useJUnitPlatform()
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
    // Pass cucumber tag filter from command line: ./gradlew test -Dtags="@driver"
    // Supports multiple tags: -Dtags="@driver or @advanced"
    systemProperty("cucumber.filter.tags", System.getProperty("tags") ?: "")

    // Change report output directory: ./gradlew test -DreportDir="my-reports"
    val reportDir = System.getProperty("reportDir")
    if (reportDir != null) {
        reports.html.outputLocation.set(file(reportDir))
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}
