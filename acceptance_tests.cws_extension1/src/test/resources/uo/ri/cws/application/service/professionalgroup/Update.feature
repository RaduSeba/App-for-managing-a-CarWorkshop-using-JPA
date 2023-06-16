@UpdateProfessionalGroup
Feature: Update a professional group
  I want to use this template for my feature file

  @IllegalArguments
  Scenario: Try to update a professional group with null arg
	    When I try to update a professional group with null arg
  	  Then argument is rejected with an explaining message
  	  
  Scenario: Try to update a professional group with null name
	    When I try to update a professional group with null name
  	  Then argument is rejected with an explaining message  	  

	  Scenario Outline: Try to update a professional group with empty name
  	  When I try to update a professional group with <name> name
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name |
 		| ""   |
 		| "   "|

	  Scenario: Try to update a professional group with negative triennium
  	  When I try to update a professional group with negative triennium
    	Then argument is rejected with an explaining message
	  
	  Scenario: Try to update a professional group with negative productivity plus
  	  When I try to update a professional group with negative productivity plus
    	Then argument is rejected with an explaining message
    	
  @BusinessException
		Scenario: Try to update a non existent professional group
    	When I try to update a non existent professional group
	    Then an error happens with an explaining message	 

	 @PositiveCases
	Scenario: Update a professional group
    When I update a professional group
    Then Triennium and productivity are updated
    And Version increases 1
    And Name does not change