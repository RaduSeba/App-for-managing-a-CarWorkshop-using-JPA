Feature: Find professional groups
  As a manager I want to find professional groups

@findById
  @IllegalArguments
  Scenario: Try to find a professional group with null id
    When I try to find a professional group with null id
    Then argument is rejected with an explaining message
    
  Scenario Outline: Try to find a professional group with wrong arg
    When I try to find a professional group with arg <arg>
    Then argument is rejected with an explaining message

    Examples: 
      | arg  |
      | "  "   | 
      | ""     | 

	@PositiveCases
	  Scenario: Find a non existent professional group
    When I search a non existing professional group
    Then professional group is not found

  Scenario: Find an existent professional group 
    When I search professional group I
    Then professional group I is found	
	
@FindAll
  Scenario: Find default 
    When I search all professional groups
    Then Default ones are found 

 Scenario: Find one more 
    Given A new registered professional group 
    When I search all professional groups
    Then Default ones are found
    And New one is found 
        	    
    