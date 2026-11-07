---
name: api-test-review
description: >
  Independently review API tests against the requirements the user
  points to. Use when the user asks for a review of an API or HTTP
  test, a Serenity Rest Assured test, or a response assertion. Does not
  rewrite tests unless the user asks separately.
---

# API test review

Review API tests that already exist. A test class, its step library, and the requirements the user names are the inputs.

Before reviewing, ask where the requirements are and whether they exist. If the user has not pointed to a source, stop and ask. A test name, a step title, a status code of 200, or a passing run is not a requirement.

## Instructions

1. Do not assume that a generated test is correct.
2. Check every test against the original requirements.
3. Check whether the assertions actually confirm the expected behavior.
4. Do not invent missing requirements.
5. Note which requirements are not covered.
6. Separate findings into Critical, Major, and Minor.
7. Do not rewrite tests automatically without a separate request.

## What to check

Read the test method and the step methods it calls. Map each requirement to the assertion that is supposed to prove it.

Check both directions. The assertion must fail when the required outcome is absent, and pass when it is present. A check that stays green for a wrong result is a finding. A check that fails while the requirement is still met is a finding.

An assertion fails this check when it passes for a wrong response. Examples: the requirement is about a field in the body, and the test only checks HTTP 200. The requirement says two values in the same payload must match, and the test compares one of them to a string written in the code. The step parses a field the requirement never mentions and asserts that.

The assertion must cover the whole outcome the requirement states. Checking the status is not a substitute for a body rule the requirement names. Do not add a field or a status the requirement does not name.

## Review standards

Treat the test code as production code. Flag duplication and HTTP calls or assertions inside the test method. Business checks stay in `@Step` methods. The test only calls them.

**DTO and contract validation check:**

- The DTO maps the API JSON contract.
- If the DTO excludes payload fields or uses `@JsonIgnoreProperties(ignoreUnknown = true)`, an inline comment explains why those fields are omitted.
- Fields ignored without that comment are a finding. The comment must say why, or the DTO must map the full contract.

Fail fast on the response, in this order:

- When the step reads the body, it checks the HTTP status first. A 500 HTML page must not fail later as a JSON parse error. The expected status is the one the requirement states. Do not invent 200 when the source says something else. When the source states no status, the status check exists only so the body can be parsed, and the finding is the parse order, not a new product rule.
- A field the requirement names is mapped and asserted. Ignoring that field is a finding. Unknown fields may be ignored only when an inline comment gives the reason.
- When the requirement says two response values must match, the test compares those values with each other. A sample JSON string written in the test is a finding.

Flag a test that leaves data behind so the next CI run depends on it.

A test is not ready to merge when it depends on a local service that CI does not have, or when the call bypasses `SerenityRest` and the report has no request. In the report each `@Step` is its own line and the HTTP call is visible. One anonymous step means the method is not `open`, or the test constructed the steps with `new`.

## Report

1. Which requirements source was used.
2. Which requirements have no test.
3. Findings, grouped Critical / Major / Minor. Each finding names the requirement and the test or step.
4. Do not include a rewritten test. Rewriting waits for a separate request.
