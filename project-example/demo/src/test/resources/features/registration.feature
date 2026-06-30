# Feature files are written utilizing Gherkin syntax: there are some recognized key words you
# will consistently use for your testing. The topmost keyword is Feature: this tells Cucumber
# what feature/userstory/capability the feature file provides scenarios and acceptance criteria for
# NOTE: each feature file should have a single Feature declared
Feature: Summer Reading Registration

# After the feature is declared you can declare your "Background" and your "Scenarios". a
# Background is shared acceptance criteria (think steps that must be taken by the user) across
# all scenarios, whereas scenarios are the actual test cases.

# When putting your acceptance criteria together you have a few keywords you can use:
# Given - this represents a starting pre-condition for the scenario
# When - this represents an action the user takes in order to progress the scenario
# Then - this represents your expected end condition, the thing you are validating
# There are a couple other keywords you can use, such as And, But, and *. 

    Scenario: Users can register with valid credentials
        Given   The user is on the login page
        When    The user clicks the registration link
        And     The user enters valid crendentials
        And     The user clicks the register button
        Then    The user should see a success message