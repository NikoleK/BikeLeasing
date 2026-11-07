Feature: Bike Theft Claim Processing

  Background:
    Given the user has an active leasing contract for the bike
    And the claim for the bike has passed the initial Intake check
    And the claim has been triaged as theft

  Scenario Outline: Processing a theft claim based on the locking condition
    Given the bike was "<locking>"
    When coverage is checked
    Then the claim is "<outcome>"
    And the insurance "<payment>"
    And a new bike is "<new bike>"

    # Assumption on the second row: the diagram states only the rejection reason.
    # "locked properly" is not written as the approval rule.
    Examples:
      | locking             | outcome  | payment      | new bike      |
      | not locked properly | rejected | does not pay | not provided  |
      | locked properly     | approved | pays         | provided      |

  Scenario: The stolen bike is found after a new bike was provided
    Given the coverage assessment was approved
    And a new bike has been provided
    When the stolen bike is found
    Then the claim state is Recovered
    # Assumption, not on the diagram or in Task 1. See features/assumptions.md.
    And the user keeps the new bike under the active contract
    And ownership of the recovered original bike transfers to Bikeleasing or the insurer

  # Assumption: this path is not in Task 1 or on the diagram. See features/assumptions.md.
  Scenario: The stolen bike was found before a new bike was provided
    Given the claim is awaiting coverage assessment
    When the stolen bike is found
    Then the claim is canceled
    And a new bike is not provided
    And the insurance does not pay
    And the user keeps the original bike

