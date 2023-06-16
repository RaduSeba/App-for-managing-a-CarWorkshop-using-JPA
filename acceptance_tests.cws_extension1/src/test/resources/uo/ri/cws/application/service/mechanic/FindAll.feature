Feature: Find all mechanics
  As a Manager
  I need to know data about all mechanics 

  Scenario: Find all mechanics, there is none
		When we read all mechanics 
		Then List of mechanics is empty
		
  Scenario Outline: Find all existing mechanic
    Given the following relation of mechanics
      | dni  	| name  | surname   | 
      | DNI-1 | Name-1| Surname-1 |
      | DNI-2 | Name-2| Surname-2 |
      | DNI-3 | Name-3| Surname-3 |
    When we read all mechanics
    Then we get the following list of mechanics 
      | mechanic								 |
      | DNI-1,Name-1,Surname-1 |
      | DNI-2,Name-2,Surname-2 |
      | DNI-3,Name-3,Surname-3 |      