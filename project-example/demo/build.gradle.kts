plugins {
	java
	// This plugin provides the "bootRun" gradle command which let's us build/start our apps
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.diffplug.spotless") version "7.0.4"
}

spotless {
	java {
		googleJavaFormat()
	}
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Put the REST Assured dependenciy above your junit dependency
	testImplementation ("io.rest-assured:rest-assured:6.0.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
	implementation("org.xerial:sqlite-jdbc:3.53.2.0")
	implementation("org.hibernate.orm:hibernate-community-dialects:8.0.0.Alpha1")
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
	testImplementation("com.h2database:h2:2.4.240")
	// the core cucumber code
    testImplementation("io.cucumber:cucumber-java:7.33.0")
	// the integration code for cucumber & junit
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.33.0")
	// the sub module that gives us access to the junit test suite feature
    testImplementation("org.junit.platform:junit-platform-suite:1.14.1")
	// This gives access to the Selenium ecosystem
	implementation("org.seleniumhq.selenium:selenium-java:4.45.0")
	// This lets us inject our cucumber test resources into our step classes
	implementation("io.cucumber:cucumber-spring:7.34.4")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
