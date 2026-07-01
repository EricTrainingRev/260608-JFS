Feature: Navigating Backwards and Forwards
  As a tester I want to practice using Selenium's navigation methods
  to move backwards and forwards through browser history

  Scenario: Navigate between pages using back and forward
    Given I am on navigation page 1
    When I click the link to page 2
    Then I should be on page 2
    When I click the link to page 3
    Then I should be on page 3
    When I navigate back
    Then I should be on page 2
    When I navigate back
    Then I should be on page 1
    When I navigate forward
    Then I should be on page 2
