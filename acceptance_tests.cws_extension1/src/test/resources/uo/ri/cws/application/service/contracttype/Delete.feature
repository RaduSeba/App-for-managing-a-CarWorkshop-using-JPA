@DeleteContractType
Feature: Delete a contract type
  As a Manager
  I need to delete a contract type. Perhaps, I typed something wrong
	 
	 @IllegalArgumentException
	  Scenario: Try to del a contract type with null arg
	  	When I try to del a contract type with null argument
  	  Then argument is rejected with an explaining message

	  Scenario Outline: Try to del a contract type with empty argument
  	  When I try to del a contract type with <name>
    	Then argument is rejected with an explaining message
 		Examples:
 		|  name  |
 		| ""   |
 		| "   "|

	 @BusinessException
		Scenario: Try to del a non existent contract type 
    	When I try to del a non existent contract type
	    Then an error happens with an explaining message	 

		Scenario: Try to del a contract type with contracts in force
    Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I | IN_FORCE |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I | IN_FORCE |
    	When I try to del contract type PERMANENT
	    Then an error happens with an explaining message	 

		Scenario: Try to del a contract type with contracts in terminated
    Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED |
    	When I try to del contract type PERMANENT
	    Then an error happens with an explaining message	 

	 @PositiveCases
	 Scenario: Del a contract type
	 		Given a contract type with no contracts
	 		When I del this contract type
	 		Then This contract type is deleted