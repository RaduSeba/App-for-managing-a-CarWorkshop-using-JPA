@DeleteContract
Feature: Delete a contract
  As a Manager
  I want to delete a contract (I wrote sth wrong and I want to delete it and then try to do it right)

  @IllegalArgumentExceptions
  Scenario Outline: Try to delete a null contract id
    When I try to delete a null contract id
    Then argument is rejected with an explaining message

  @BusinessExceptions
  Scenario Outline: Try to delete a non existing contract
    When I try to delete a non existing contract
    Then an error happens with an explaining message

  Scenario Outline: Try to delete a contract for a mechanic with workorders during the contract
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type      | group | state    | startDate |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | IN_FORCE | 1-1-2022  |
    And several payrolls for all the contracts
    When I try to delete the contract
    Then an error happens with an explaining message

  Scenario Outline: Try to delete a contract for a mechanic with payrolls
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type      | group | state    |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | IN_FORCE |
    #Given the following mechanic
      #| dni   | name   | surname   |
      #| DNI-2 | Name-2 | Surname-2 |
    #Given the following relation of contracts for this mechanic
      #| type      | group | state    | annual_money | startDate | endDate |
      #| PERMANENT | I     | IN_FORCE |        20000 | 1-1-2022  |         |
    And several payrolls for all the contracts
    When I try to delete the contract
    Then an error happens with an explaining message

  @PositiveScenarios
  Scenario: Delete a contract in force
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type      | group | state    |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | IN_FORCE |
    When I delete the contract
    Then This contract does not exist any more

  Scenario: Delete a contract terminated
    Given the following mechanic with a contract terminated
      | dni   | name   | surname   | type      | group | state      |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | TERMINATED |
    When I delete the contract
    Then This contract does not exist any more
