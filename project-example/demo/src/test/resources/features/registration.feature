# Feature files are written utilizing Gherkin syntax: there are some recognized key words you
# will consistently use for your testing. The topmost keyword is Feature: this tells Cucumber
# what feature/userstory/capability the feature file provides scenarios and acceptance criteria for
# NOTE: each feature file should have a single Feature declared
Feature: Summer Reading Registration

# After the feature is declared you can declare your "Background" and your "Scenarios". a
# Background is shared acceptance criteria (think steps that must be taken by the user) across
# all scenarios, whereas scenarios are the actual test cases.

    Background: all users navigate to the registration page
        Given   The user is on the login page
        When    The user clicks the registration link

# When putting your acceptance criteria together you have a few keywords you can use:
# Given - this represents a starting pre-condition for the scenario
# When - this represents an action the user takes in order to progress the scenario
# Then - this represents your expected end condition, the thing you are validating
# There are a couple other keywords you can use, such as And, But, and *. 

    Scenario: Users can register with valid credentials
        And     The user enters valid crendentials
        And     The user clicks the register button
        Then    The user should see a success message

# Whenever you have a scenario that needs to run multiple times but with different inputs each
# execution you should use a Scenario Outline. This ties together with an "Examples" table that
# lets you inject test data from your feature file directly at test time
# in the example below the username, password, and message values are going to be injected at
# test time from an Examples table

    Scenario Outline: Users can not register with invalid credentials
        And     The user enters username "<username>" and password "<password>"
        And     The user clicks the register button
        Then    The user should see failure message "<message>"

# Here in the Examples table we can organize our test data to validate different features and/or 
# requirements are being enforced at runtime. This system synergizes very well with decision tables,
# equivalence partitioning, and boundary value analysis

    Examples:
    |username|password|message|
    |shrt|P0ssword|Username should be between 5 and 15 characters|
    |Thisiswaytoolong|P0ssword|Username should be between 5 and 15 characters|