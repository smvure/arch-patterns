# arch-patterns — reference code

Runnable Java reference implementations for the architecture patterns at
**[svure.me/patterns](https://svure.me/patterns)**. Each pattern page links here; each **module**
here mirrors the code shown on its page — complete and test-backed so you can run it, step
through it, and modify it.

Built for **Java 25 LTS** — the examples use record patterns, unnamed variables (`_`), sealed
types, and virtual threads.

## Layout

A Gradle **multi-project build**: one module per pattern, one root.

```
arch-patterns/
├── settings.gradle.kts        includes the modules
├── build.gradle.kts           root (nothing to configure)
├── gradle.properties          parallel + build cache
├── buildSrc/                  shared build logic
│   └── src/main/kotlin/svure.java-conventions.gradle.kts   ← Java 25 toolchain + JUnit, in ONE place
├── cqrs/                      → svure.me/patterns/cqrs/
│   ├── build.gradle.kts       applies the convention plugin + sets the demo main class
│   └── src/{main,test}/java/me/svure/patterns/cqrs/
└── strangler-fig/             → svure.me/patterns/strangler-fig/
    ├── build.gradle.kts
    └── src/{main,test}/java/me/svure/patterns/stranglerfig/
```

Each module's `build.gradle.kts` is deliberately tiny — the toolchain and test setup live once in
the `svure.java-conventions` plugin. Add a new pattern by creating `newpattern/` with that plugin
and its own `mainClass`, then `include("newpattern")` in `settings.gradle.kts`.

## Requirements

- **JDK 25** (Temurin recommended). With [SDKMAN](https://sdkman.io): `sdk install java 25-tem`,
  then `sdk env` in this folder (reads `.sdkmanrc`).
- Gradle is provided by the build (see below).

## Run it

### In VS Code (easiest)
Open the folder, accept the recommended extensions (Java Extension Pack + Gradle for Java), and the
tooling imports all modules. Run any `*Test` from the Test Explorer, or any `*Demo` via the **Run**
lens above `main`.

### On the command line
The Gradle wrapper binary is not committed. Generate it once, then use it:

```bash
gradle wrapper --gradle-version 9.1.0   # one-time; needs Gradle on PATH (brew install gradle)

./gradlew test                 # run every module's tests
./gradlew :cqrs:run            # run the CQRS demo
./gradlew :strangler-fig:run   # run the Strangler Fig demo
./gradlew :cqrs:test           # just one module's tests
```

If you already have Gradle 9.0+ installed, run `gradle …` directly without the wrapper.

## The tests are the spec
- **cqrs** — the write model rejects an over-reservation while the read model keeps projecting the
  correct available count.
- **strangler-fig** — legacy / cut-over / shadow routing each behave correctly, and shadow mode
  serves the legacy price while detecting the modern engine's 10% divergence.

## License

MIT — see [LICENSE](LICENSE).
