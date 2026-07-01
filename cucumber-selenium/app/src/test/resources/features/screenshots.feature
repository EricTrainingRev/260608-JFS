Feature: Taking Screenshots
  As a tester I want to practice taking screenshots with Selenium
  to capture the state of the page during test execution

  Scenario: Toggle between images and capture screenshots
    Given I am on the screenshot practice page
    Then the page should display "Image 1"
    When I take a screenshot named "image1_displayed"
    When I click the button to show image 2
    Then the page should display "Image 2"
    When I take a screenshot named "image2_displayed"
    When I click the button to show image 3
    Then the page should display "Image 3"
    When I take a screenshot named "image3_displayed"
