Feature: Find mechanics in professional groups
  As a manager I want to find mechanics in professional groups

@findMechanicsByProfessionalGroupId
  @IllegalArguments
  Scenario: Try to find mechanics for a null professional group id
    When I try to find mechanics for a null professional group id
    Then argument is rejected with an explaining message
    
  Scenario Outline: Try to find mechanics for a wrong professional group id
    When I try to find mechanics for a wrong professional group id <arg>
    Then argument is rejected with an explaining message

    Examples: 
      | arg  |
      | "  "   | 
      | ""     | 

	@PositiveCases
	  Scenario: Find mechanics for a non existent professional group
    When I search mechanics in a non existing professional group
    Then zero mechanics are found

  Scenario: Find mechanics for an existent professional group with no mechanics 
    When I search mechanics in professional group I
    Then zero mechanics are found
    
  Scenario: Find mechanics for an existent professional group with mechanics
    Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE | 01-01-2022 |
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |IN_FORCE |01-01-2022 |
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |IN_FORCE |01-01-2022 |
      | DNI-4 | Name-4| Surname-4 | PERMANENT | II |IN_FORCE |01-01-2022 |
      | DNI-5 | Name-5| Surname-5 |PERMANENT | II |IN_FORCE |01-01-2022 |
      | DNI-6 | Name-6| Surname-6 |PERMANENT | II |IN_FORCE |01-01-2022 |
    When I search mechanics in professional group I
    Then we get the following list of mechanics
      | mechanic |
      | DNI-1,Name-1,Surname-1 |
      | DNI-2,Name-2,Surname-2 |
      | DNI-3,Name-3,Surname-3 |

    
        	    
    