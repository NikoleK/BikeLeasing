# Bikeleasing QA Technical Assignment

This repository contains the solution for the Bikeleasing QA Engineer technical assessment.
Task 1 is a set of business feature files for the claims workflow. Task 2 is a small runnable Serenity project in Kotlin. The feature files are not executed: Task 2 asks for JUnit and Serenity steps, not Cucumber.

## Tech stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| Language | Kotlin 2.3 | JVM language used for the tests |
| Build tool | Gradle 9.2 | Dependencies and test run |
| JDK | Temurin 25 | Same version as `jvmToolchain` and CI |
| Test runner | JUnit Jupiter | Test engine |
| Reporting | Serenity BDD | Steps and the HTML report |
| API client | Serenity Rest Assured | HTTP calls recorded in the Serenity report |
| CI | GitHub Actions | Runs the tests and uploads the report |

## Repository structure

```text
.
├── features/                         # Task 1: feature files, not executed
│   ├── theft.feature                 # Theft: reject, approve, found later
│   ├── damage.feature                # Partial payment or total loss
│   ├── defect_or_wear.feature        # Routed out to warranty
│   └── assumptions.md                # Assumptions and questions for a PO
│
├── src/test/
│   ├── kotlin/
│   │   ├── api/
│   │   │   ├── CatFactAPITests.kt    # API test
│   │   │   ├── CatFactsSteps.kt      # API steps
│   │   │   └── CatFactModels.kt      # data, fact, length only
│   │   └── ui/
│   │       ├── BikeleasingUITests.kt # Web test
│   │       └── BikeleasingUISteps.kt # Open the page, check host and logo
│   └── resources/
│       └── serenity.conf             # URLs and Chrome settings
│
├── .github/
│   ├── workflows/tests.yml           # CI job
│   └── skills/                       # Instructions for a coding agent
└── build.gradle.kts
```

## Task 1: BDD feature files

The scenarios follow the claims diagram. Comments in the feature files mark rules that are assumptions. The same notes, plus open questions, are in [features/assumptions.md](features/assumptions.md). Those notes are not requirements.

1. **Theft** (`theft.feature`): a claim is rejected or approved. After a new bike is provided, the stolen bike can still be found.
2. **Damage** (`damage.feature`): partial payment or total loss.
3. **Defect and wear** (`defect_or_wear.feature`): the claim is routed out.

`./gradlew test` does not run these files. There are no step definitions and no Cucumber runner.

`.github/skills/feature-authoring/SKILL.md` writes feature files from the requirements or diagram you point it at. It asks about anything the source does not settle and records that question instead of inventing a rule. `.github/skills/bdd-review/SKILL.md` reviews feature files. The claims diagram is `.github/skills/bdd-review/claims-diagram.png`.

## Task 2: automated tests

### Web (`BikeleasingUITests`)

Opens `https://www.bikeleasing.de` from `web.base.url` and waits until the host is `bikeleasing.de`. A redirect may change `www` or the scheme. The check compares the host, not one exact URL string.

An extra step checks that the header image with alt text `Bikeleasing Logo` is visible and that its source contains `logo-bikeleasing`. That check is not in the assignment text.

The browser is the `@Managed` driver on the test class. Each step receives it as an argument.

### API (`CatFactAPITests`)

Requests 5 facts from `https://catfact.ninja/facts`. The step checks HTTP 200 before reading the body. It then checks that five facts came back and that the first fact's `length` matches the text.

The model maps only `data`, `fact`, and `length`. Pagination and any other unknown JSON fields are ignored with `@JsonIgnoreProperties(ignoreUnknown = true)`.

## Run the tests

Both tests, then the Serenity report:

```bash
./gradlew clean test
```

`test` is `finalizedBy("aggregate")`, so the report is built even when a test fails.

One class:

```bash
./gradlew test --tests ui.BikeleasingUITests
./gradlew test --tests api.CatFactAPITests
```

If the HTML is stale:

```bash
./gradlew aggregate --rerun-tasks
```

Open the report:

```bash
open target/site/serenity/index.html
```

`target/` is gitignored. The HTML is produced by the test run. It is not stored in git.

To see the browser window locally, set `headless.mode = false` in `serenity.conf` and remove `--headless=new` from `chrome.switches`.

`web.base.url` can be overridden with the environment variable `WEB_BASE_URL`.

## GitHub Actions

`.github/workflows/tests.yml` is one job. It runs on GitHub after a push or a pull request. A local `./gradlew test` does not start it.

The job installs Temurin 25 and Chrome, runs `./gradlew test --no-daemon`, and uploads `target/site/serenity` as the `serenity-report` artifact. The artifact is uploaded even when a test fails. Download it from the Actions run.

The job runs Task 2 only.

## Agent skills

`.github/skills/` holds markdown instructions for a coding agent. They are not a test framework, and CI does not execute them.

| Skill | Use |
| :--- | :--- |
| `serenity-web` | Add or change a web test |
| `serenity-api` | Add or change an API test |
| `ui-test-review` | Review a web test against a requirement you point to |
| `api-test-review` | Review an API test against a requirement you point to |
| `bdd-review` | Review feature files against requirements you point to |
