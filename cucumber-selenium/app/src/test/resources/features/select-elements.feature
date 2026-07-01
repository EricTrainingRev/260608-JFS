@driver
Feature: Interacting with Select Elements
  As a tester I want to practice using Selenium's Select class
  to interact with dropdown and multi-select elements

  Scenario: Select options from a single and multi-select dropdown
    Given I am on the select practice page
    When I select "Java" from the single select dropdown by visible text
    Then the selected language should display "Java"
    When I select the option at index 2 from the single select dropdown
    Then the selected language should display "Python"
    When I select "javascript" from the single select dropdown by value
    Then the selected language should display "JavaScript"
    When I select "Selenium" and "Cucumber" from the multi select
    Then the selected skills should display "Selenium, Cucumber"
    When I deselect all options from the multi select
    Then the selected skills should display "None"
