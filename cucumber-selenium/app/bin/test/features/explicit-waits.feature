@driver
Feature: Explicit Waits
  As a tester I want to practice using Selenium's explicit waits
  to handle elements that appear or change after a delay

  Scenario: Wait for elements to appear, become clickable, and change text
    Given I am on the explicit wait practice page
    When I click the load content button
    Then I should see the delayed content within 5 seconds
    When I click the enable submit button
    Then the submit button should become clickable within 5 seconds
    When I click the submit button
    Then I should see the confirmation message
    When I click the update status button
    Then the status text should change to "Status: Complete" within 5 seconds
    When I click the dismiss banner button
    Then the banner should disappear within 5 seconds
