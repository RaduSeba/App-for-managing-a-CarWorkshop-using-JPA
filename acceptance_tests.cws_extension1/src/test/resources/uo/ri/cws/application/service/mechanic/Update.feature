Feature: Update a mechanic
  As a Manager
  I want to update a mechanic

  Scenario: Update an existing mechanic
    Given a mechanic
    When I update the mechanic 
    Then the mechanic results updated for fields name and surname

  Scenario: Try to update an non existing mechanic
    When I try to update a non existing mechanic
    Then an error happens with an explaining message

  Scenario Outline: Try to update a mechanic with null name
    When I try to update a mechanic with null name
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a mechanic with null surname
    When I try to update a mechanic with null surname
    Then argument is rejected with an explaining message

  Scenario Outline: Try to update a mechanic with null dni
    When I try to update a mechanic with null dni
    Then argument is rejected with an explaining message
            
  Scenario Outline: Try to update with wrong parameters
    Given a mechanic
    When I try to update the mechanic to <name>, <surname> and <dni>
    Then argument is rejected with an explaining message

    Examples: 
      | dni   					  | name 				| surname   | 
      | "existing dni" 		| "Mechanic1" | ""        |
      | "existing dni" 		| "Mechanic1" | "     "   |
      | "existing dni" 		| "" 					| "surname1"|
      | "existing dni" 		| "         "	| "surname1"|
      | "" 								| "Mechanic1" | "surname1"|
      | "           " 		| "Mechanic1" | "surname1"|
