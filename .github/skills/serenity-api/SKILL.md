---
name: serenity-api
description: >
  Add or change a Serenity BDD API test in Kotlin and JUnit 5.
  Use when calling an HTTP API with Serenity Rest Assured, checking
  a status code, or asserting a response field in a @Step.
  Starts from the behavior the user asked for, then implements it.
---

# Serenity API tests

Add an API check as a JUnit test plus a step library. Write code only after the requested behavior is clear. The test calls steps. The steps send the request and check that outcome.

## Requirements first

The user says what the test must prove. That statement is the requirement. A file, ticket, or message all count. A test name or a status of 200 does not.

1. Restate the request and the expected outcome in one or two sentences. Say what is out of scope.
2. If the outcome is missing or can be read two ways, ask. Do not fill the gap.
3. Do not add a status check, field, or scenario the request does not mention.
4. Every assertion proves one stated outcome. A check that would pass for a wrong body does not go in the test.
5. The assertion passes when the required outcome is present and fails when it is not. A check that stays green for a wrong result does not count. A check that fails while the required outcome is still true does not count either.

Then implement.

## Layers

| Layer | Does | Does not |
|---|---|---|
| Test class | `@ExtendWith(SerenityJUnit5Extension::class)`, `@Steps`, passes inputs, receives results | URLs, HTTP calls, assertions |
| Step library | One request or one check per `@Step` method | Setup of the HTTP client inside the test |
| `serenity.conf` | Base URL and paths | Expected body values |
| Model | The API JSON contract for this response | Assertions. An assertion is added only when the requirement names that field |

Inject steps with `@Steps`. Do not construct them with `new`, or the call is missing from the Serenity report.

The step class and every `@Step` method are `open`.

Read the base URL and path from `serenity.conf`. Fail at once when a key is missing. Do not hardcode the host in the step.

Call the API through `net.serenitybdd.rest.SerenityRest`, so the request appears in the report.

## Request and checks

Split the call and the business checks.

1. One step sends the request. When the requirement names an HTTP status, check that status before reading the body, then return the parsed body.
2. Later steps assert the body. Each `@Step` checks one outcome.

Assert the values the requirement names. When it says two fields in the response must match, compare those two fields. Do not paste a sample payload into the test as the expected text.

Use JUnit assertions. Order is expected, then actual. The message says what failed and includes the actual value. Do not add another assertion library in the same change.

**Model (DTO) default rule:** By default, map and validate the complete API JSON contract. **Exception:** If fields are ignored (for example via `@JsonIgnoreProperties(ignoreUnknown = true)` for pagination or metadata), it must be explicitly justified by the test scope or the requirements, and it must include an inline code comment explaining why those fields are omitted.

Do not ignore a field the requirement names, a failed assertion, a missing config key, or a status the requirement states.

## Done

A new or changed test is done only after a compile and three passing runs in a row.

```bash
./gradlew compileTestKotlin
./gradlew test --tests <fully.qualified.TestClass>
```

Run the test command three times. Any failure resets the count. Fix the cause, then start from zero.

Open `target/site/serenity/index.html`. The request and each `@Step` are visible. One anonymous step means the method is not `open`, or the test used `new`.

## When a run fails

- **Environment** — API unreachable, timeout, or a 5xx that is not the behavior under test. Do not weaken the assertion.
- **Product** — status or body does not match the agreed check. Keep the assertion.
- **Test** — wrong path, wrong field, or a hardcoded body. Fix the test.

Loosening a check to get a green run is not a fix.
