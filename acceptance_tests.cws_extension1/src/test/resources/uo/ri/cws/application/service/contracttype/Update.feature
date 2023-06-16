@UpdateContractType
Feature: Update a contract type
  I want to use this template for my feature file

  @IllegalArguments
  Scenario: Try to update a contract type with null arg
    When I try to update a contract type with null arg
    Then argument is rejected with an explaining message

  Scenario: Try to update a contract type with null name
    When I try to update a contract type with null name
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a contract type with empty name
    When I try to update a contract type with <name> name
    Then argument is rejected with an explaining message

    Examples: 
      | name  |
      | ""    |
      | "   " |

  Scenario: Try to update a contract type with negative days
    When I try to update a contract type with negative days
    Then argument is rejected with an explaining message

  @BusinessException
  Scenario: Try to update a non existent contract type
    When I try to update a non existent contract type
    Then an error happens with an explaining message

  @PositiveCases
  Scenario: Update an existent contract type
    When I update contract type PERMANENT
    Then Compensation days field is updated
    And Contract type Version increases 1
    And Contract type name does not change

  Scenario: Update an existent contract type repeatedly
    When I update contract type PERMANENT repeatedly
    Then Compensation days field is updated
    And Contract type Version increases 2
    And Contract type name does not change
