Feature: XPath Locators
  As a tester I want to practice using XPath expressions
  to locate elements on a web page

  Scenario: Locate elements using various XPath strategies
    Given I am on the xpath practice page
    When I enter "Alice" into the first name field using XPath by id
    And I enter "Smith" into the last name field using XPath by name
    And I enter "alice@test.com" into the email field using XPath by placeholder
    Then the first name field should contain "Alice"
    And the last name field should contain "Smith"
    When I locate the second row of the employee table using XPath indexing
    Then the employee name in that row should be "Bob Smith"
    When I locate all notifications that contain the text "Error" using XPath contains
    Then I should find 1 matching notification
