// Shared Java setup for every pattern module. Applied via: plugins { id("svure.java-conventions") }
plugins {
    java
}

group = "me.svure.patterns"
version = "0.1.0"

java {
    // Examples use Java 25 LTS features (record patterns, unnamed variables, virtual threads).
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging { events("passed", "skipped", "failed") }
}
