Feature: Generate payrolls for mechanics this current month
  As a Manager
  I want to generate payslips for my employees 

  @IllegalArguments
  Scenario: Generate payrolls for no mechanic
    When I generate payrolls  
    Then zero payrolls are generated

 Scenario: Generate payrolls for no mechanic in force this month
     Given the following mechanic with a contract terminated
      | dni  	| name  | surname   | type | group | state |
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED |
    When I generate payrolls  
    Then zero payrolls are generated
    
    Scenario Outline: Generate payrolls for a mechanic in force with no workorders
    Given today is <present>   
    And A contract in force for a mechanic with <startdate> start date and <annualMoney> base salary 
    When I generate payrolls  
    Then one payroll is generated with <monthlyMoney>, <bonus>, <productivity>, <triennium>, <tax>, <nic>
    
    Examples:
       | dni  	| name  | surname   | type | group | state | startdate 		| annualMoney |  monthlyMoney	| bonus 	| productivity 	| triennium | tax 			| nic		| present |
  | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE  | "01-06-2020" 	| 8400.0			|  600.0					| 600.0		| 0.0										| 0.0						| 228.0	| 35.0	| "31-12-2021"		|
   | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2009" 	| 8400.0			|  600.0					| 600.0		| 0.0										| 186.96  	| 263.52 | 35.0	| "31-12-2021"		|
   | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2020" 	| 8400.0			|  600.0					| 0.0					| 0.0										| 0.0						| 114.0	| 35.0	| "30-11-2021"		|
  | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2009" 	| 8400.0			|  600.0					| 0.0		      | 0.0										| 186.96  	| 149.52	 | 35.0	| "30-11-2021"		|

| DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE    | "01-06-2020" 	| 84000.0			|  6000.0					| 6000.0		| 0.0										| 0.0						| 5400.0	| 350.0	| "31-12-2021"		|
    | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2009" 	| 84000.0			|  6000.0					| 6000.0		| 0.0										|186.96	  	| 5484.13	 | 350.0	| "31-12-2021"		|
    | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2020" 	| 84000.0			|  6000.0					| 0.0					| 0.0										| 0.0						| 2700.0	| 350.0	| "30-11-2021"		|
    | DNI-1 | Name-1| Surname-1 | PERMANENT | I | IN_FORCE| "01-06-2009" 	| 84000.0			|  6000.0					| 0.0		      | 0.0										| 186.96  	| 2784.13	 | 350.0	| "30-11-2021"		|
    
   Scenario Outline: Generate payrolls for a mechanic in force with workorders
    Given today is <present> 
    And A contract in force for a mechanic with <startdate> start date and <annualMoney> base salary 
    And Exactly <n> invoiced workorders for the mechanic with amount <amount>
    When I generate payrolls
    Then one payroll is generated with <monthlyMoney>, <bonus>, <productivity>, <triennium>, <tax>, <nic>
    
    Examples:
    | startdate 						| annualMoney 	|  monthlyMoney	| bonus 		| productivity 		| triennium			 | tax 				| nic			|  n | amount		| present |
    | "01-06-2021" 	| 8400.0						| 600.0								| 600.0		| 135.0									| 0.0						| 253.65	| 35.0	|  3 | 900.0			|  "30-12-2021" |
    | "01-06-2009" 	| 8400.0						| 600.0								| 600.0		| 135.0									| 186.96		| 289.17  | 35.0	| 3 | 900.0			| "30-12-2021" |
    | "01-06-2021" 	| 8400.0						| 600.0								| 0.0					| 135.0									| 0.0						| 139.65  | 35.0	|  3 | 900.0  | "30-11-2021" |
    | "01-06-2009" 	| 8400.0						| 600.0								| 0.0					| 135.0									| 186.96		| 175.17	| 35.0	|  3 | 900.0  | "30-11-2021" |


    | "01-06-2021" 	| 84000.0						| 6000.0								| 6000.0		| 135.0									| 0.0						| 5460.75	| 350.0	|  3 | 900.0			|  "30-12-2021" |
    | "01-06-2009" 	| 84000.0						| 6000.0								| 6000.0		| 135.0									| 186.96		| 5544.88  | 350.0	| 3 | 900.0			| "30-12-2021" |
    | "01-06-2021" 	| 84000.0						| 6000.0								| 0.0			  		| 135.0									| 0.0						| 2760.75  | 350.0	|  3 | 900.0  | "30-11-2021" |
    | "01-06-2009" 	| 84000.0						| 6000.0								| 0.0					  | 135.0									| 186.96		| 2844.88	| 350.0	|  3 | 900.0  | "30-11-2021" |

  Scenario Outline: Generate payrolls for a mechanic with a  contract terminated the same month as payroll
     Given today is <present> 
    And A contract terminated for a mechanic, with <startdate> start date, <present> end date, <annualMoney> base salary 
    And Exactly <n> invoiced workorders for the mechanic with amount <amount>
    When I generate payrolls
    Then one payroll is generated with <monthlyMoney>, <bonus>, <productivity>, <triennium>, <tax>, <nic>
    
    Examples:
    
    | startdate 						| annualMoney 	|  monthlyMoney	| bonus 		| productivity 		| triennium			 | tax 				| nic			|  n | amount		| present |
    | "01-06-2021" 	| 8400.0						| 600.0								| 600.0		| 135.0									| 0.0						| 253.65	| 35.0	|  3 | 900.0			|  "30-12-2021" |
    | "01-06-2009" 	| 8400.0						| 600.0								| 600.0		| 135.0									| 186.96		| 289.17  | 35.0	| 3 | 900.0			| "30-12-2021" |
    | "01-06-2021" 	| 8400.0						| 600.0								| 0.0					| 135.0									| 0.0						| 139.65  | 35.0	|  3 | 900.0  | "30-11-2021" |
    | "01-06-2009" 	| 8400.0						| 600.0								| 0.0					| 135.0									| 186.96		| 175.17	| 35.0	|  3 | 900.0  | "30-11-2021" |
    | "01-06-2021" 	| 84000.0						| 6000.0								| 6000.0		| 135.0									| 0.0						| 5460.75	| 350.0	|  3 | 900.0			|  "30-12-2021" |
    | "01-06-2009" 	| 84000.0						| 6000.0								| 6000.0		| 135.0									| 186.96		| 5544.88  | 350.0	| 3 | 900.0			| "30-12-2021" |
    | "01-06-2021" 	| 84000.0						| 6000.0								| 0.0			  		| 135.0									| 0.0						| 2760.75  | 350.0	|  3 | 900.0  | "30-11-2021" |
    | "01-06-2009" 	| 84000.0						| 6000.0								| 0.0					  | 135.0									| 186.96		| 2844.88	| 350.0	|  3 | 900.0  | "30-11-2021" |
   
   
       
   
   Scenario Outline: Generate payrolls for a mechanic with a terminated contract long ago
    Given today is
    | present |
    | 30-8-2021 |
    Given the following mechanic with a contract terminated
      | dni  	| name  | surname   | type | group | state | startDate | endDate | 
      | DNI-1 | Name-1| Surname-1 | PERMANENT | I | TERMINATED | 1-1-2021 | 1-5-2021 |
    When I generate payrolls  
    Then zero payrolls are generated
        
  Scenario: Generate payrolls for mechanics in force only
     Given today is
    | present |
    | 30-8-2021 |
       Given the following relation of mechanics with a contract
      | dni  	| name  | surname   | type | group | state | startDate | endDate |
      | DNI-7 | Name-7| Surname-7 | PERMANENT | I | TERMINATED | 1-1-2021 | 1-5-2021 |
      | DNI-8 | Name-8| Surname-8 | PERMANENT | I |TERMINATED | 1-1-2021 | 1-5-2021 |
      | DNI-9 | Name-9| Surname-9 | PERMANENT | I |TERMINATED | 1-1-2021 | 1-5-2021 |
      |DNI-10 | Name-10| Surname-10 | TEMPORARY | II |IN_FORCE | 1-1-2021 | 1-12-2021 |
      | DNI-11 | Name-11| Surname-11 | TEMPORARY | II |IN_FORCE | 1-1-2021 | 1-12-2021 |
    When I generate payrolls  
    Then two payrolls are generated