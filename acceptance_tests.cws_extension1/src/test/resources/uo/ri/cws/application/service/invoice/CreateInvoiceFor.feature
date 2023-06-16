Feature: Create one invoice for one or multiple jobs
  As a Cashier
  I want to create an invoice for several jobs done for a single client 
  Because I want to get paid

  Scenario: Create one invoice for an existing workorder
    Given a client registered
    And a vehicle
    And one finished workorder
    When I create an invoice for the workorders
    Then an invoice is created
    
  Scenario: Create one invoice for multiple existing workorders
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    When I create an invoice for the workorders
    Then an invoice is created
      
  Scenario: Trying to create one invoice there is one non existing workorder
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And one non existent workorder  
    When I try to create an invoice
    Then an error happens with an explaining message
      
  Scenario: Trying to create one invoice there is one workorder ASSIGNED 
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And one ASSIGNED workorder  
    When I try to create an invoice
    Then an error happens with an explaining message
  
    Scenario: Trying to create one invoice there is one workorder OPEN 
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And one OPEN workorder  
    When I try to create an invoice
    Then an error happens with an explaining message
    
  Scenario: Trying to create one invoice there is one workorder INVOICED 
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And one INVOICED workorder  
    When I try to create an invoice
    Then an error happens with an explaining message
    
  Scenario: Trying to create one invoice for a null argument
    When I try to create an invoice for a null list
    Then argument is rejected with an explaining message
    
  Scenario: Trying to create one invoice for an empty argument
    When I try to create an invoice for an empty list
    Then argument is rejected with an explaining message
    
  Scenario: Trying to create one invoice and one of the id is null 
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And a null id
    When I try to create an invoice for a wrong argument 
    Then argument is rejected with an explaining message
      
  Scenario: Trying to create one invoice and one of the id is emtpy 
    Given a client registered
    And a vehicle
    And a list of several finished workorder ids
    And an empty id
    When I try to create an invoice for a wrong argument 
    Then argument is rejected with an explaining message