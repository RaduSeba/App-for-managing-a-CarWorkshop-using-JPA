Feature: Add a mechanic
  As a Manager
  I want to register a mechani 
  Because we need a new worker

  Scenario: Add a non existing mechanic
    When I add a new non existing mechanic
    Then the mechanic results added to the system

  Scenario: Try to add a mechanic with a repeated dni
    Given a mechanic
    When I try to add a new mechanic with same dni
    Then an error happens with an explaining message

  Scenario Outline: Try to add a mechanic with null argument
    When I try to add a new mechanic with null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a mechanic with null dni
    When I try to add a new mechanic with null dni
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a mechanic with invalid dni
    When I try to add a new mechanic with <dni>, <name>, <surname> 
    Then argument is rejected with an explaining message

    Examples: 
      | dni   | name 				| surname   | 
      | "" 		| "Mechanic1" | "surname" |
      | "   " | "Mechanic1" | "surname" |
