Feature: Delete all Payrolls this month
  As a manager
  I want to remove all payrolls generated this month, if any, due to some error

  @No_payroll_2_del
  Scenario: There is no payroll at all
    When I delete this month payrolls
    Then There is no payroll left

  Scenario: There is no payroll this month
    Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
  	And several payrolls before this month
    When I delete this month payrolls
    Then There is no payroll left 
    
	@Several_payrolls_this_month
	@Several_payrolls_not_this_month   
	Scenario Outline: There are payrolls this month and others not this month
    Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate | 
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE | 1-1-2021| 
  	#And several payrolls
     And several payrolls for all the contracts
  	
    When I delete this month payrolls
    Then This month payroll is deleted  
    
  @DeleteTwice
 	Scenario: I delete payrolls this month twice in a row
      Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE |
 # 	And several payrolls
       And several payrolls for all the contracts
 
    When I delete this month payrolls twice
    Then No payroll is deleted   
    
	@AllPayrollsThisMonth
  Scenario Outline: There is no payroll other month than this month
    Given today is
    |present|
    |30-3-2021|   
    Given the following mechanic with a contract in force
      | dni  	| name  | surname   | type | group | state | startDate | present |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE | 1-1-2021 | 30-3-2021|
  #	And several payrolls this month only
         And several payrolls for all the contracts
  
    When I delete this month payrolls
    Then There is no payroll left    	
	 