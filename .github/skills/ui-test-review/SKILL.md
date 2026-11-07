---
name: ui-test-review
description: >
  Independently review UI tests against the requirements the user
  points to. Use when the user asks for a review of a web or UI test,
  a Serenity web test, or a @Step that drives a browser. Does not
  rewrite tests unless the user asks separately.
---

# UI test review

Review UI tests that already exist. A test class, its step library, and the requirements the user names are the inputs.

Before reviewing, ask where the requirements are and whether they exist. If the user has not pointed to a source, stop and ask. A test name, a step title, or a passing run is not a requirement.

## Instructions

1. Do not assume that a generated test is correct.
2. Check every test against the original requirements.
3. Check whether the assertions actually confirm the expected behavior.
4. Do not invent missing requirements.
5. Note which requirements are not covered.
6. Separate findings into Critical, Major, and Minor.
7. Do not rewrite tests automatically without a separate request.
8. Stay within the requested review scope. Do not review API tests during a UI-test review unless the user asks for both.

## What to check

Read the test method and the step methods it calls. Map each requirement to the assertion that is supposed to prove it.

Check both directions. The assertion must fail when the required outcome is absent, and pass when it is present. A check that stays green for a wrong result is a finding. A check that fails while the requirement is still met is a finding.

An assertion fails this check when it passes for a wrong outcome. Examples: the step only opens a page, and the assertion checks that the driver is not null. The requirement says a button label, and the assertion checks that any element exists. The requirement allows a redirect, and the assertion demands one exact URL string.

Also note a locator or wait only when it makes the assertion prove the wrong thing or nothing: the element is found by copy or index, the check runs before the page has reached the expected state, or the test contains the locator and the assertion instead of the step.

## Review standards

Treat the test code as production code. Flag duplication, a test that holds locators or assertions, and a step library mixed with driver setup. Business actions stay in `@Step` methods. The test only calls them.

The assertion must cover the whole outcome the requirement states. A click that should change a total is not proved by an icon becoming visible when the requirement also states the price, the tax, or the quantity. Do not add an outcome the requirement does not name. A missing part of a named outcome is a finding. An extra check you thought of is not.

Flag a brittle UI test:

- `Thread.sleep`, or an implicit wait stacked on an explicit wait.
- A locator used before the page is ready, or tied to copy, a layout `div` chain, or an element index.
- No explicit wait for the state the assertion checks.
- A browser or test data left open after a failure, so the next CI run depends on it. Serenity closes the `@Managed` driver. A driver the test opened itself must be closed on failure too.

A test is not ready to merge when it needs a visible browser or a local pause to pass. The headless CI run must be deterministic. In the Serenity report each `@Step` is its own line. A web step that used the `@Managed` driver has a screenshot. One anonymous step means the method is not `open`, or the test constructed the steps with `new`.

## Report

1. Which requirements source was used.
2. Show requirement coverage in a compact table with columns for requirement, test/step, and coverage status. Explicitly list requirements with no test.
3. Show findings in a table grouped by Critical / Major / Minor. Each finding names the requirement and the test or step.
4. Do not include a rewritten test. Rewriting waits for a separate request.
