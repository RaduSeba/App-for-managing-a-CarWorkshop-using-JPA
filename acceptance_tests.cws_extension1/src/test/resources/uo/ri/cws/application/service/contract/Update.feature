Feature: Update a contract
  As a manager I want to update some fields

  @IllegalArguments
  Scenario: Try to update a contract with null arg
    When I try to update a contract with null arg
    Then argument is rejected with an explaining message

  Scenario: Try to update a contract with null id
    When I try to update a contract with null id
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a contract with wrong id
    When I try to update a contract with id <ident>
    Then argument is rejected with an explaining message

    Examples: 
      | ident |
      | "  "  |
      | ""    |

  Scenario Outline: Try to update a contract with wrong wage
    When I try to update a contract with wrong wage <money>
    Then argument is rejected with an explaining message

    Examples: 
      | money  |
      |    0.0 |
      | -100.0 |

  @BusinessExceptions
  Scenario: Try to update a non existing contract
    When I try to update a non existing contract
    Then an error happens with an explaining message

  Scenario: Try to update a contract terminated
    Given the following mechanic with a contract terminated
      | dni   | name   | surname   | type      | group | state      | startDate | endDate   |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | TERMINATED | 1-1-2022  | 25-5-2022 |
    When I try to update a terminated contract
    Then an error happens with an explaining message

  Scenario: Try to update a contract with wrong end date
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type       | group | state    | startDate | endDate  |
      | DNI-1 | Name-1 | Surname-1 | FIXED_TERM | I     | IN_FORCE | 1-1-2022  | 1-1-2025 |
    When I try to update a fixed term contract with end date earlier than start date
    Then an error happens with an explaining message

  @OK
  Scenario: Update only end date
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type       | group | state    | startDate | endDate  | pay   |
      | DNI-1 | Name-1 | Surname-1 | FIXED_TERM | I     | IN_FORCE | 1-1-2022  | 1-1-2025 | 30000 |
    When I update the end date of the contract to
      | newEndDate |
      | 1-12-2025  |
    Then updatable fields are updated and all other fields remain the same

  Scenario: Update only wage
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type       | group | state    | startDate | endDate  | pay   |
      | DNI-1 | Name-1 | Surname-1 | FIXED_TERM | I     | IN_FORCE | 1-1-2022  | 1-1-2025 | 30000 |
    When I update wage of the contract to
      | wage    |
      | 50000.0 |
    Then updatable fields are updated and all other fields remain the same

  Scenario: Update both end date and wage
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type       | group | state    | startDate | endDate  | pay   |
      | DNI-1 | Name-1 | Surname-1 | FIXED_TERM | I     | IN_FORCE | 1-1-2022  | 1-1-2025 | 30000 |
    When I update end date and wage of the contract to
      | newWage | newEndDate |
      | 50000.0 | 1-5-2025   |
    Then updatable fields are updated and all other fields remain the same
