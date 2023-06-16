Feature: Add a new professional group
  As a Manager
  I want to add a new professional group due to changes in labour laws
	 
	 @IllegalArgumentException
	  Scenario: Try to add a professional group with null arg
	  	When I try to add a professional group with null argument
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to add a professional group with null name
  	  When I try to add a professional group with null name
    	Then argument is rejected with an explaining message
    	
	  Scenario Outline: Try to add a professional group with empty name
  	  When I try to add a professional group with <name>
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name  | 
 		| ""   |
 		| "   "|

	  Scenario: Try to add a professional group with negative triennium
  	  When I try to add a professional group with negative triennium
    	Then argument is rejected with an explaining message
	  
	  Scenario: Try to add a professional group with negative productivity plus
  	  When I try to add a professional group with negative productivity plus
    	Then argument is rejected with an explaining message

	 
	 @BusinessException
		Scenario: Try to add a repeated professional group
    	When I try to add a repeated professional group
	    Then an error happens with an explaining message	 

	 @PositiveCases
	Scenario: Add a new professional group
	  When I register a new professional group with the following data
    	  	|  name  | trienniumPay | productivityRate |
	  	| X     | 20.0 								| 3.3 |
    Then The professional group is added
    | professionalGroup|
    |X,20.0,3.3|