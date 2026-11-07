Feature: Bike Defect and Wear Claim Routing

  Background:
    Given the user has an active leasing contract for the bike
    And the claim for the bike has passed the initial Intake check

  Scenario Outline: Routing defect and wear claims to the warranty route
    Given the bike issue is "<issue>"
    When the claim is triaged
    Then the claim is routed out to the warranty route
    And the insurance does not pay

    Examples:
      | issue  |
      | defect |
      | wear   |
