Feature: Add a contract for a mechanic
  As a Manager
  I want to hire a mechanic

  Scenario: Hire an existing mechanic with no previous contract in force
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-1 | Name-1 | Surname-1 |
    When I hire the mechanic with this data
      | type      | group | pay    |
      | PERMANENT | I     | 2000.0 |
    Then there is a contract in force for the mechanic with the following data
      | dni   | name   | surname   | type      | group | pay    | settlement |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | 2000.0 |          0 |
    And contract version is 1
    And start date is first day next month

  Scenario: Hire an existing mechanic with a previous contract in force shorter than a year
    Given the following mechanic with a contract in force shorter than a year
      | dni   | name   | surname   | type      | group | state    | startDate | pay    |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | IN_FORCE | 1-01-2022 | 2000.0 |
    When I hire the mechanic with this data
      | type      | group | pay    |
      | PERMANENT | I     | 2000.0 |
    Then there is a contract in force for the mechanic with the following data
      | dni   | name   | surname   | type      | group | pay    | settlement |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | 2000.0 |          0 |
    And contract version is 1
    And start date is first day next month
    And previous contract is terminated

  Scenario: Hire an existing mechanic with a contract in force longer than a year
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type      | group | state    | startDate | pay     |
      | DNI-2 | Name-2 | Surname-2 | PERMANENT | I     | IN_FORCE | 1-01-2021 | 16800.0 |
    And several payrolls for all the contracts
    When I hire the mechanic with this data
      | dni   | name   | surname   | type      | group | pay     |
      | DNI-2 | Name-2 | Surname-2 | PERMANENT | I     | 12000.0 |
    Then there is a contract in force for the mechanic with the following data
      | dni   | name   | surname   | type      | group | pay     | settlement |
      | DNI-2 | Name-2 | Surname-2 | PERMANENT | I     | 12000.0 |          0 |
    And contract version is 1
    And start date is first day next month
    And previous contract is terminated

  #16800 / 365 * 1 anho de antiguedad * 1.35 dias / anho trabajado
  Scenario: Hire an existing mechanic with previous contract terminated
    Given the following mechanic with a contract terminated
      | dni   | name   | surname   | type      | group | state      | startDate | pay    |
      | DNI-3 | Name-3 | Surname-3 | PERMANENT | I     | TERMINATED | 1-01-2022 | 2000.0 |
    When I hire the mechanic with this data
      | type      | group | pay    |
      | PERMANENT | I     | 2000.0 |
    Then there is a contract in force for the mechanic with the following data
      | dni   | name   | surname   | type      | group | pay    | settlement |
      | DNI-3 | Name-3 | Surname-3 | PERMANENT | I     | 2000.0 |          0 |
    And contract version is 1
    And start date is first day next month

  @BusinessExceptions
  Scenario Outline: Try to add a contract for a non existing mechanic
    When I try to add a contract for a non existing mechanic
    Then an error happens with an explaining message

  Scenario Outline: Try to add a contract for a non existing contract type
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-4 | Name-4 | Surname-4 |
    When I try to add a contract for a non existing contract type
    Then an error happens with an explaining message

  Scenario Outline: Try to add a contract for a non existing professional group
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-5 | Name-5 | Surname-5 |
    When I try to add a contract for a non existing professional group
    Then an error happens with an explaining message

  Scenario Outline: Try to add a contract with end date not later than start date when mandatory
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-6 | Name-6 | Surname-6 |
    When I try to add a contract with end date not later than start date
    Then an error happens with an explaining message

  @IllegalArgumentExceptions
  Scenario Outline: Try to add a null contract
    When I try to add a null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a contract with null dni
    When I try to add a contract with null dni
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a contract with wrong dni
    When I try to add a contract with wrong dni <mechDNI>
    Then argument is rejected with an explaining message

    Examples: 
      | mechDNI |
      | "   "   |
      | ""      |

  Scenario Outline: Try to add a contract with null contract type id
    When I try to add a contract with null contract type id
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a contract with null professional group id
    When I try to add a contract with null professional group id
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a contract with null end date when mandatory
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-6 | Name-6 | Surname-6 |
    When I try to add a contract with null end date for FIXED_TERM contract type
    Then argument is rejected with an explaining message

  Scenario Outline: Try to add a contract with wrong values
    When I try to add a contract with wrong fields <type> <profGroup> <annualWage>
    Then argument is rejected with an explaining message

    Examples: 
      Examples:

      | type        | profGroup | annualWage |
      | "   "       | "I"       |     1000.0 |
      | ""          | "I"       |     1000.0 |
      | "PERMANENT" | " "       |     1000.0 |
      | "PERMANENT" | ""        |     1000.0 |
      | "PERMANENT" | "I"       |        0.0 |
      | "PERMANENT" | "I"       |    -1000.0 |
