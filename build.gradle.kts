// Root of the arch-patterns multi-project build.
//
// Each pattern is its own module (see settings.gradle.kts) and applies the shared
// `svure.java-conventions` plugin from buildSrc/. There is nothing to configure here.
//
//   ./gradlew test                 run every module's tests
//   ./gradlew :cqrs:run            run the CQRS demo
//   ./gradlew :strangler-fig:run   run the Strangler Fig demo
