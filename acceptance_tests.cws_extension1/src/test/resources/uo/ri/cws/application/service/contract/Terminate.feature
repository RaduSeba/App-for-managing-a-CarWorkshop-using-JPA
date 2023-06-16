@terminateContract
Feature: Terminate a contract
  As a manager I want to terminate a contract

  @IllegalArgumentExceptions
  Scenario: Try to terminate a contract with null id
    When I try to terminate a contract with null id
    Then argument is rejected with an explaining message

  Scenario Outline: Try to terminate a contract with wrong id
    When I try to terminate a contract with wrong <id>
    Then argument is rejected with an explaining message

    Examples: 
      | id   |
      | ""   |
      | "  " |

  @BusinessExceptions
  Scenario: Try to terminate a non existent contract
    When I try to terminate a non existent contract
    Then an error happens with an explaining message

  Scenario: Try to terminate a terminated contract
    Given the following mechanic with a contract terminated
      | dni   | name   | surname   | type      | group | state      | startDate | end_date  |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | TERMINATED | 1-1-2022  | 25-5-2022 |
    When I try to terminate a terminated contract
    Then an error happens with an explaining message

  @OK
  Scenario: Terminate a contract in force
    Given the following mechanic with a contract in force for one year
      | dni   | name   | surname   | type      | group | pay     |
      | DNI-2 | Name-2 | Surname-2 | PERMANENT | I     | 16800.0 |
    And several payrolls for all the contracts
    When I terminate the contract
    Then There is no contract in force for the mechanic
    And previous contract is terminated with this settlement
      | settlement |
      |      62.13 |  
    # (16800 / 365) * 1 anho de antiguedad * 1.35 dias / anho trabajado
      
