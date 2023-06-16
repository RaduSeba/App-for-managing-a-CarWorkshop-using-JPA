Feature: Find payrolls all scenarios
  As a manager I want to find information about payrolls

	@getAllPayrolls
		Scenario: Find payrolls when there is none 
    	When I search payrolls
    	Then Result is empty
    	
		Scenario: Find payrolls when there are some 
		Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I | IN_FORCE |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I | IN_FORCE |
    And several payrolls for all the contracts     
  	When I search payrolls
   	Then All payrolls summary are returned
    	
  @getPayrollDetails
  	@IllegalArgumentException
	  Scenario: Try to find a payroll with null id
	  	When I try to find payroll with null argument
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to find a payroll with invalid id
  	  When I try to find payroll with <id> argument
    	Then argument is rejected with an explaining message
 		Examples:
 		|  id  |
 		| ""   |
 		| "   "|
	  
		@PositiveCases
		Scenario: Find a non existent payroll 
    	When I find a payroll with a non existent id
    	Then Returns empty
    	
		Scenario: Find an existent payroll 
     Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE | 1-5-2022|
  	And One payroll for this contract this month 
   	When I search a payroll by id
   	Then This payroll is returned
    	
	@getAllPayrollsForMechanic
  	@IllegalArgumentException
	  Scenario: Try to find a payroll with null mechanic id
	  	When I try to find payrolls with null mechanic id
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to find a payroll with invalid mechanic id
  	  When I try to find payrolls with mechanic <id> argument
    	Then argument is rejected with an explaining message
 		Examples:
 		|  id  |
 		| ""   |
 		| "   "|
 			
  	@BusinessException
		Scenario: Find payrolls for a non existent mechanic 
    	When I try to find payrolls with a non existent id
	    Then an error happens with an explaining message
 			
		@PositiveCases
	
	  Scenario: Find payrolls for a mechanid with no payroll 
     Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
	    When I search payrolls for the mechanic
    	Then Result is empty
	    
	  Scenario: Find payrolls for a mechanid with one payroll 
     Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
	    And One payroll for this contract this month
	    When I search payrolls for the mechanic
	    Then The payroll summary for this mechanic is found
	    
	  Scenario: Find payrolls for a mechanid with several payroll 
     Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
    And several payrolls 
	    When I search payrolls for the mechanic
	    Then All payroll summaries for this mechanic are found     			
 					
	@getAllPayrollsForProfessionalGroup
  	@IllegalArgumentException
	  Scenario: Try to find a payroll with null professional group name
	  	When I try to find payrolls with null professional group name
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to find a payroll with invalid professional group name
  	  When I try to find payrolls with professional group <name> argument
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name  |
 		| ""   |
 		| "   "|
 			
  	@BusinessException
		Scenario: Find payrolls for a non existent professional group 
    	When I try to find payrolls with a non existent professional group name
	    Then an error happens with an explaining message
 			
		@PositiveCases
	
	  Scenario: Find payrolls for a professional group with no payroll 
    Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |IN_FORCE |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |IN_FORCE |
	    When I search payrolls for the professional group 
    	Then Result is empty
	    
	  Scenario: Find payrolls for a professional group with one payroll 
     Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
	    And One payroll for this contract this month
 	    When I search payrolls for the professional group
	    Then The payroll summary for this professional group is found
	    
	  Scenario: Find payrolls for a professional group with several payroll 
	  Given  the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |IN_FORCE |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |IN_FORCE |
       And several payrolls for all the contracts
	    When I search payrolls for the professional group 
    	Then All payroll summaries for this professional group are found
	  
	  