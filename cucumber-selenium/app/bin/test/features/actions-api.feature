@advanced
Feature: Actions API
  As a tester I want to practice using Selenium's Actions API
  to perform complex user interactions like drag and drop

@current
  Scenario: Drag the red box onto the yellow box to create an orange box
    Given I am on the actions practice page
    Then the red box should be visible
    And the yellow box should be visible
    And the orange box should not be visible
    When I drag the red box onto the yellow box
    Then the orange box should be visible
    And the red box should not be visible
    And the yellow box should not be visible
    And the status message should display "Status: Boxes combined into orange!"
