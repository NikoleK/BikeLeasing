# Bikeleasing Serenity Kotlin PoC

This repository contains a small proof-of-concept test project built with Gradle, Kotlin, JUnit Jupiter, and Serenity BDD.

The project demonstrates two automated checks:

- a UI test that opens the Bikeleasing website and checks that the host is `bikeleasing.de`;
- an API test that requests cat facts from catfact.ninja, verifies the number of returned facts, and checks the reported length of the first fact.

`features/` holds the claim scenarios for theft, damage, and defect or wear. Gradle does not run those files.

## Tech stack

- Gradle Wrapper
- Kotlin
- JUnit Jupiter
- Serenity BDD
- Serenity REST Assured
- Selenium/WebDriver managed by Serenity

## Run the tests

```bash
./gradlew clean test
```

The Serenity report is written to `target/site/serenity/index.html`. `target/` is gitignored.

One class:

```bash
./gradlew test --tests ui.BikeleasingUITests
./gradlew test --tests api.CatFactAPITests
```

## Feature files

The scenarios follow the claims diagram. Comments in the feature files mark assumptions. The same notes, plus open questions, are in [features/assumptions.md](features/assumptions.md).

- `theft.feature`: a claim is rejected or approved. After a new bike is provided, the stolen bike can still be found.
- `damage.feature`: partial payment or total loss.
- `defect_or_wear.feature`: the claim is routed out.

## GitHub Actions

`.github/workflows/tests.yml` runs on a push, a pull request, or a manual run from the Actions tab. The job installs Temurin 25 and Chrome, runs `./gradlew test --no-daemon`, and uploads the Serenity report. The report is uploaded even when a test fails.

## Agent skills

`.github/skills/` holds instructions for a coding agent. CI does not execute them.

- `serenity-web`: add or change a web test
- `serenity-api`: add or change an API test
- `ui-test-review`: review a web test against a requirement you point to
- `api-test-review`: review an API test against a requirement you point to
- `feature-authoring`: write feature files from a requirement or diagram you point to
- `bdd-review`: review feature files against requirements you point to
