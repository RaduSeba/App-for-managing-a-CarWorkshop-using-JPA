Feature: Find contract type
  
  @FindById
  @IllegalArguments
  Scenario Outline: Try to find a contract type with wrong name
    When I try to find a contract type with name <arg>
    Then argument is rejected with an explaining message

    Examples: 
      | arg  |
      | "  "   | 
      | ""     | 

	@PositiveCases
	  Scenario: Find a non existent contract type
    When I search a non existing contract type
    Then contract type is not found

  Scenario: Find an existent contract type 
    When I search contract type PERMANENT
    Then contract type PERMANENT is found	
      
  @FindAll
   Scenario: Find default 
    When I search all contract types
    Then "3" contract types are found
    And Default contract types are found 

 Scenario: Find one more 
    Given A new registered contract type 
    When I search all contract types
    Then Default contract types are found 
    And New contract type is found 