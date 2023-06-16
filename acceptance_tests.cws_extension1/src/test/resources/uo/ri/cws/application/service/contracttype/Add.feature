Feature: Add a new contract type
  As a Manager
  I want to add a contract type group due to changes in labour laws
	 
	 @IllegalArgumentException
	  Scenario: Try to add a contract type with null arg
	  	When I try to add a contract type with null argument
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to add a contract type with null name
  	  When I try to add a contract type with null name
    	Then argument is rejected with an explaining message
    	
	  Scenario Outline: Try to add a contract type with empty name
  	  When I try to add a contract type with <name>
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name  |
 		| ""   |
 		| "   "|

	  Scenario: Try to add a contract type with negative compensation days
  	  When I try to add a contract type with negative compensation days
    	Then argument is rejected with an explaining message
	  

	 @BusinessException
		Scenario: Try to add a repeated contract type
    	When I try to add a contract type with name PERMANENT
	    Then an error happens with an explaining message	 

	 @PositiveCases
	Scenario: Add a new contract type
    When I register a new contract type
    | name    | days |
    | name |   10.0 |
    Then The contract type is added
        | type |
        | name,10.0 |