Feature: Delete a mechanic
  As a Manager
  I want to delete a mechanic
  in case have not been used to keep the system clean

  Scenario: Delete an existing mechanic with no work orders, no contracts
    Given a mechanic
    When I remove the mechanic
    Then the mechanic no longer exists

  Scenario: Try to remove a non existing mechanic
    When I try to remove a non existent mechanic
    Then an error happens with an explaining message

  Scenario: Try to remove a mechanic with work orders
    Given a mechanic with work orders registered
    When I try to remove the mechanic
    Then an error happens with an explaining message
    
   Scenario: Try to remove a mechanic with terminated contracts 
       Given the following mechanic with a contract terminated
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED |
    When I try to remove the mechanic
    Then an error happens with an explaining message 
          
  Scenario: Try to remove a mechanic with contract in force 
       Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I |  IN_FORCE |
    When I try to remove the mechanic
    Then an error happens with an explaining message    

  Scenario Outline: Try to delete a mechanic with null id
    When I try to remove a mechanic with null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to delete a mechanic with wrong fields
    When I try to delete a mechanic with <dni>
    Then argument is rejected with an explaining message

    Examples: 
      | dni    |
      | "   " |
      | ""    |
