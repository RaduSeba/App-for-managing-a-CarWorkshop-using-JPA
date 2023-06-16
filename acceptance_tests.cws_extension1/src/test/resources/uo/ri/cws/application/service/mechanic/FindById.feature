Feature: Find a mechanic by id
	As a Manager
	I need to recover a mechanic by id
	To see its details

  Scenario Outline: Find an existing mechanic
    Given the following mechanic
      | dni  	| name  | surname   | 
      | DNI-1 | Name-1| Surname-1 | 
	  When I look for mechanic by id
	    Then one mechanic is found
      And I get the following mechanic
      | mechanic								 |
      | DNI-1,Name-1,Surname-1 | 

  Scenario: Try to find a mechanic with non existing id
		When I try to find a non existent mechanic
		Then mechanic not found

  Scenario Outline: Try to find a mechanic with null argument
    When I try to find a mechanic with null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to find a mechanic with wrong parameters
    When I look for a mechanic by wrong id <id>
    Then argument is rejected with an explaining message

    Examples: 
      | id   			| 
			| "" 					| 
      | "         "	|		