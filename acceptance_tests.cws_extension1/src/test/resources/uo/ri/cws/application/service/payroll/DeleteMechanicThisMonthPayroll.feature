Feature: Delete this month payroll for a mechanic 
  As a Manager
  I want to delete current month payroll for a mechanic

 
	@IllegalArguments    
   
  Scenario: Try to delete last payroll with null mechanid dni
    When I try to delete with null argument
    Then argument is rejected with an explaining message

  Scenario Outline: Try to delete last payroll with invalid mechanid dni
    When I try to delete with wrong mechanic <dni> argument
    Then argument is rejected with an explaining message

 		Examples:
 		|  dni  |
 		| ""   |
 		| "   "|
 		
	@BusinessExceptions    
  Scenario: Try to delete current month payroll for a non existent mechanid 
    When I try to delete current month payroll for a non existent mechanid
    Then an error happens with an explaining message
            
	@PositiveCases

  Scenario: Delete current month payroll for a mechanid with no payroll 
      Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
    When I delete the current month payroll for the mechanic
    Then There is no payroll for this mechanic anymore
    
  Scenario: Delete current month payroll for a mechanid with only one payroll, this month
      Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
  	And One payroll for this contract this month
    When I delete the current month payroll for the mechanic
    Then There is no payroll for this mechanic anymore
    
  Scenario: Delete current month payroll for a mechanid with several payroll
  
      Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate | 
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE | 1-1-2021| 
     And several payrolls for all the contracts
    When I delete the current month payroll for the mechanic
    Then Current month payroll for this mechanic is deleted and the rest remain    