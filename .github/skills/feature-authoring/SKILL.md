---
name: feature-authoring
description: >
  Write Gherkin feature files from requirements or a diagram the user
  points to. Use when the user asks for a feature file, a scenario, or
  BDD specifications. Asks about anything unclear and records that
  question instead of guessing a business rule.
---

# Feature files

Write feature files that say what the business does. Do not describe clicks, field ids, or other UI mechanics.

This skill writes features. It does not review them and it does not add step definitions.

## Before writing

Ask where the requirements are and where the diagram is, if the user has not already pointed to them. A feature name is not a source. If neither a requirement nor a diagram is available, stop and ask. Do not draft rules from memory.

Read the source, then separate two lists:

1. Rules the source states.
2. Points the source does not settle. A missing arrow, an unnamed outcome, and a word that can be read two ways belong here.

Show both lists to the user and ask about every open point before turning it into a `Then`. Write the confirmed scenarios while waiting. For each open point, add a comment and no guessed step:

```gherkin
# Question: what happens when the contract is invalid? The diagram asks, and does not draw an outcome.
```

Do not fill that gap with a `Then`. A later answer can replace the comment with a scenario.

## How a feature is shaped

One file per business area. The file reads on its own.

```gherkin
Feature: <business outcome>
  High-level description of the business capability, rules, and outcomes under test.

  Background:
    Given <precondition shared by every scenario in this file>

  Scenario: <one outcome>
    Given <state>
    When <business action>
    Then <observable outcome>
```

`Background` holds only what is already true for every scenario in the file. A fact that differs between scenarios stays in the scenario.

Use a `Scenario Outline` and `Examples` when the steps stay the same and only the data changes. When the outcomes differ in kind, write separate scenarios.

A step states the business action or outcome:

- `When the customer submits a damage claim`
- `When the customer clicks "#claim-submit"` is not a feature step
- `And the customer enters "500" into the "amount" field` is not a feature step

One scenario checks one outcome. Do not add a rule, branch, or example the source does not contain.

## When the source and the draft disagree

The source wins. If a scenario needs a fact the source does not give, stop on that scenario, ask, and leave the `# Question:` comment. The rest of the file can still be written.
