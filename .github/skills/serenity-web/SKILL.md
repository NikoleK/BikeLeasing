---
name: serenity-web
description: >
  Add or change a Serenity BDD web test in Kotlin and JUnit 5.
  Use when opening a page, checking a URL, or writing a @Step that
  uses WebDriver. Starts from the behavior the user asked for,
  then adds the test class, step library, locators, and waits.
---

# Serenity web tests

Add a web check as a JUnit test plus a step library. Write code only after the requested behavior is clear. The step performs that action, waits, and asserts that outcome.

## Requirements first

The user says what the test must prove. That statement is the requirement. A file, ticket, or message all count. A test name does not.

1. Restate the action and the expected outcome in one or two sentences. Say what is out of scope.
2. If the outcome is missing or can be read two ways, ask. Do not fill the gap.
3. Do not add a scenario, assertion, locator, or browser the request does not mention.
4. Every assertion proves one stated outcome. A check that would pass for a wrong page does not go in the test.
5. The assertion passes when the required outcome is present and fails when it is not. A check that stays green for a wrong result does not count. A check that fails while the required outcome is still true does not count either.

Then implement.

## Layers

| Layer | Does | Does not |
|---|---|---|
| Test class | `@ExtendWith(SerenityJUnit5Extension::class)`, `@Managed` driver, `@Steps`, calls steps in order | Locators, waits, assertions, URLs |
| Step library | One business action or check per `@Step` method | A second `WebDriver` field |
| `serenity.conf` | Base URL, driver, headless switches | Assertions |

`@Managed` is filled only on the test class. A `@Managed` field on a step stays null.

Inject the step library with `@Steps`. Do not construct it with `new`, or the method is missing from the Serenity report.

The step class and every `@Step` method are `open`. Kotlin makes them final otherwise, and Serenity cannot record the step.

Read the page address from `serenity.conf`. Fail at once when the key is missing. Do not hardcode the host in the step.

## Browser

The default run is headless Chrome, the same browser CI uses. The three acceptance runs use that configuration. A new scenario does not get a browser matrix on day one.

Add Firefox, Edge, or Safari when the scenario can fail differently in that browser: layout, CSS, file download, date input, or a bug already seen in one engine. Add it as an extra target in `serenity.conf` under `environments`, and keep Chrome as the default. A URL or redirect check does not need a second browser.

A visible window is for watching a failure locally. It is not an extra acceptance run. For one debug run, set `headless.mode = false` and remove `--headless=new` from `chrome.switches`, then put both back.

## Locators

Locators live in the step library. The test never names an element.

Prefer a stable hook, in this order: `data-testid` or `id`, then a CSS class or attribute that belongs to the behavior, not to the layout. Use Serenity's `$("#id")`, `$(".class")`, or `$(By.cssSelector("..."))`. A string that starts with a tag name, such as `$("button")`, is read as XPath and matches the wrong thing.

Do not locate by visible copy, by a long XPath through layout `div`s, or by element index. Copy changes, and index changes when a block moves.

A locator that the scenario does not act on or assert does not get added. Serenity element methods (`click()`, `waitUntilVisible()`, `getText()`) already wait. Do not add a second wait in front of them, and do not use `Thread.sleep`.

## Wait and assert

Wait for the state the requirement names, then assert that state. A navigation check waits until the URL has settled, then asserts the host or path the user asked for. It does not invent a tighter URL.

- Use `WebDriverWait` or a Serenity wait. About 10 seconds is enough. The timeout message names the expected value.
- The assertion message includes the actual URL.
- JUnit order is expected, then actual.
- No `Thread.sleep`.
- Do not set an implicit wait in the same test as an explicit wait. The two timeouts stack.

## Done

A new or changed test is done only after a compile and three passing runs in a row. Use the headless Chrome configuration.

```bash
./gradlew compileTestKotlin
./gradlew test --tests <fully.qualified.TestClass>
```

Run the test command three times. Any failure resets the count. Fix the cause, then start from zero.

Open `target/site/serenity/index.html`. Each `@Step` is its own line. One anonymous step means the method is not `open`, or the test used `new`.

## When a run fails

- **Environment** — no Chrome, site unreachable, timeout only on CI. Do not weaken the assertion.
- **Product** — the page does not end on the expected URL. Keep the assertion.
- **Test** — the check ran before the redirect finished, or the expected host was hardcoded. Fix the test.

Loosening a check to get a green run is not a fix.
