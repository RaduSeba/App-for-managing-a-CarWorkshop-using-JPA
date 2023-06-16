Feature: Find all mechanics with contract in force
  As a Manager
  I need to know current mechanics 

  Scenario: Find all existing mechanics with contract in force when there is no mechanic
    When we read all mechanics with contract in force
		Then List of mechanics is empty

  Scenario Outline: Find all mechanics with contract in force when there is no mechanic with contract in force
        Given the following relation of mechanics with a contract terminated
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED |1-01-2022|
      | DNI-2 | Name-2| Surname-2 | PERMANENT | I |TERMINATED |1-01-2022|
      | DNI-3 | Name-3| Surname-3 | PERMANENT | I |TERMINATED |1-01-2022|
      When we read all mechanics with contract in force
		Then List of mechanics is empty
            
  Scenario Outline: Find all existing mechanics with contract in force when all mechanics have a contract in force
      Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-4 | Name-4| Surname-4 | PERMANENT | I | IN_FORCE |1-01-2022 |
      | DNI-5 | Name-5| Surname-5 | PERMANENT | I |IN_FORCE |1-01-2022 |
      | DNI-6 | Name-6| Surname-6 | PERMANENT | I |IN_FORCE |1-01-2022 |
    When we read all mechanics with contract in force
    Then we get the following list of mechanics 
      | mechanic								 |
      | DNI-4,Name-4,Surname-4 |
      | DNI-5,Name-5,Surname-5 |
      | DNI-6,Name-6,Surname-6 | 		
      
  Scenario Outline: Find all existing mechanics with contract in force when there are some mechanics with contract in force
   Given the following relation of mechanics with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-7 | Name-7| Surname-7 | PERMANENT | I | IN_FORCE | 1-01-2022 |
      |DNI-8 | Name-8| Surname-8| PERMANENT | I |IN_FORCE |1-01-2022 |
      |  DNI-9 | Name-9| Surname-9 | PERMANENT | I |IN_FORCE |1-01-2022 |
    And the following relation of mechanics with a contract terminated
      | dni  	| name  | surname   | type | group | state | startDate |
      | DNI-10 | Name-10| Surname-10 | PERMANENT | II |TERMINATED |1-01-2022 |
      | DNI-11 | Name-11| Surname-11 |PERMANENT | II |TERMINATED|1-01-2022 |
          When we read all mechanics with contract in force
    Then we get the following list of mechanics 
      | mechanic								 |
      | DNI-7,Name-7,Surname-7 |
      | DNI-8,Name-8,Surname-8 |
      | DNI-9,Name-9,Surname-9 | 	            