@DeleteProfessionalGroup
Feature: Delete a professional group
  As a Manager
  I need to delete a professional group. Perhaps, I typed something wrong
	 
	 @IllegalArgumentException
	  Scenario: Try to del a professional group with null arg
	  	When I try to del a professional group with null argument
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to del a professional group with empty argument
  	  When I try to del a professional group with <name>
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name  |
 		| ""   |
 		| "   "|

	 @BusinessException
		Scenario: Try to del a non existent professional group 
    	When I try to del a non existent professional group
	    Then an error happens with an explaining message	 

		Scenario: Try to del a professional group involved in some contract 
		Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |IN_FORCE |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |IN_FORCE |
    	When I try to del professional group I
	    Then an error happens with an explaining message	 
	    
	Scenario: Try to del a professional group involved in some terminated contract 
    Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |TERMINATED|
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |TERMINATED|
    	When I try to del professional group I
	    Then an error happens with an explaining message	 
	    

	 @PositiveCases
	 Scenario: Del a professional group with no contracts
	 		Given a freshly new professional group
	 		When I del this professional group
	 		Then The professional group is deleted