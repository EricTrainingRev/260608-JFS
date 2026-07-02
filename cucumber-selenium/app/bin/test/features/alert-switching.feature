@driver
Feature: Alert Switching
  As a tester I want to practice switching to and handling browser alerts
  including simple alerts, confirm dialogs, and prompt dialogs

  Scenario: Handle simple, confirm, and prompt alerts
    Given I am on the alert practice page
    When I click the simple alert button
    And I accept the alert
    Then the simple alert result should display "Simple alert was handled."
    When I click the confirm alert button
    And I dismiss the alert
    Then the confirm alert result should display "You clicked Cancel."
    When I click the confirm alert button
    And I accept the alert
    Then the confirm alert result should display "You clicked OK."
    When I click the prompt alert button
    And I type "Selenium" into the alert and accept
    Then the prompt alert result should display "Hello, Selenium!"
