Feature: Bike Damage Claim Processing

  Background:
    Given the user has an active leasing contract for the bike
    And the claim for the bike has passed the initial Intake check
    And the claim has been triaged as damage

  Scenario Outline: Processing a damage claim based on damage type
    Given the bike damage is "<damage>"
    When coverage is checked
    Then the claim is settled as "<outcome>"
    And the insurance "<payment>"
    And wear parts are "<wear parts>"

    # Assumption on the total-loss row: the diagram says "Total loss / Not worth repairing"
    # and that insurance pays fully or partly. It does not say "pays in full"
    # or that wear parts are "not itemised".
    Examples:
      | damage              | outcome    | payment      | wear parts   |
      | worth repairing     | partial    | partly pays  | not paid     |
      | not worth repairing | total loss | pays in full | not itemised |
