@FindContracts
Feature: Find contracts
  As a manager I want to find an specific contract by id
  and also all contracts for a mechanic by mechanic dni
  and also all contracts

  @IllegalArguments
  Scenario: Try to find a contract with null id
    When I try to find a contract with null id
    Then argument is rejected with an explaining message

  Scenario: Try to find a contract for a mechanic with null arg
    When I try to find a contract for a mechanic with null id
    Then argument is rejected with an explaining message

  @FindById
  Scenario: Find by id a non existent contract
    When I search a non existing contract id
    Then Contract is not found

  Scenario: Find by id an existent contract in force
    Given the following mechanic with a contract in force
      | dni   | name   | surname   | type      | group | state    |
      | DNI-1 | Name-1 | Surname-1 | PERMANENT | I     | IN_FORCE |
    When I search the contract in force
    Then Contract is found

  Scenario: Find by id an existent contract terminated
    Given the following mechanic with a contract terminated
      | dni   | name   | surname   | type      | group | state      |
      | DNI-2 | Name-2 | Surname-2 | PERMANENT | I     | TERMINATED |
    When I search the contract terminated
    Then Contract is found

  @FindByMechanicId
  Scenario: Find contracts by mechanic id for a non existent mechanic
    When I search contracts for a non existent mechanic
    Then List of contracts summary is empty

  Scenario: Find contracts by mechanic id for an existent mechanic with no contracts
    When I search contracts for a non existent mechanic
    Then List of contracts summary is empty

  Scenario: Find contracts by mechanic id for an existent mechanic with no contracts in force and one terminated contract
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-3 | Name-3 | Surname-3 |
    And the following relation of contracts for this mechanic
      | type      | group | state      | startDate | endDate    |
      | PERMANENT | I     | TERMINATED | 1-1-2000  | 30-11-2000 |
      | PERMANENT | II    | TERMINATED | 1-1-2002  | 30-6-2002  |
    When I search contracts for the mechanic
    Then All contracts summary are found
    And There is no contract in force

  Scenario: Find contracts by mechanic id for existent mechanic with several contracts terminated and one contract in force with no payrolls
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-4 | Name-4 | Surname-4 |
    Given the following relation of contracts for this mechanic
      | type      | group | state      | startDate | endDate    |
      | PERMANENT | I     | TERMINATED | 1-1-2000  | 30-11-2000 |
      | PERMANENT | II    | IN_FORCE   | 1-1-2022  |            |
    When I search contracts for the mechanic
    Then All contracts summary are found

  Scenario: Find contracts by mechanic id for an existent mechanic with several contracts terminated and one contract in force with several payrolls
    Given the following mechanic
      | dni   | name   | surname   |
      | DNI-5 | Name-5 | Surname-5 |
    And the following relation of contracts for this mechanic
      | type      | group | state      | startDate | endDate    |
      | PERMANENT | I     | TERMINATED | 1-1-2000  | 30-11-2000 |
      | PERMANENT | II    | IN_FORCE   | 1-1-2022  |            |
    And several payrolls for all the contracts
    When I search contracts for the mechanic
    Then All contracts summary are found
