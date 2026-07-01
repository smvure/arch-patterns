plugins {
    // Auto-downloads the JDK 25 toolchain if it isn't already installed.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "arch-patterns"

include("cqrs", "strangler-fig")
