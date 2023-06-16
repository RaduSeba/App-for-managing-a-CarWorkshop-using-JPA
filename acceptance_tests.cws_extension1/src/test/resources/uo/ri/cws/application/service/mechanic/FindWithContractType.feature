Feature: Find mechanics with contract in force in contract type
  
    
  @FindMechanicsWithContractInFoceInContractType
  
  @IllegalArguments
  Scenario: Try to find mechanics with null contract type
    When I try to find a mechanic with null contract type
    Then argument is rejected with an explaining message
    
  Scenario Outline: Try to find a mechanics with wrong contract type
    When I try to find mechanics with contract type <arg>
    Then argument is rejected with an explaining message

    Examples: 
      | arg  |
      | "  "   | 
      | ""     | 

	@PositiveCases
	  Scenario: Find mechanics in a non existent contract type
    When I search mechanics in a non existing contract type
    Then an empty list is returned

  Scenario: Find mechanics in an existent contract type with no contracts
 Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED | 1-1-2022 |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |TERMINATED |1-1-2022 |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |TERMINATED |1-1-2022 |
    When I search mechanics in contract type PERMANENT
    Then an empty list is returned
    
  Scenario: Find mechanics in an existent contract type 
     Given the following relation of mechanics with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |1-01-2022 |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |IN_FORCE |1-01-2022|
      | DNI-4 | Name-4| Surname-4 | TEMPORARY | II |IN_FORCE |1-01-2022|
    And the following relation of mechanics with a contract terminated  
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |TERMINATED |1-01-2022 |
    When I search mechanics in contract type PERMANENT
    Then we get the following list of mechanics 
      | mechanic								 |
      | DNI-1,Name-1,Surname-1 |
      | DNI-2,Name-2,Surname-2 |