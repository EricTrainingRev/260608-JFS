package com.example.demo.cucumber.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ExampleSteps {

    @Given("The user is on the login page")
    public void the_user_is_on_the_login_page() {
        System.out.println("Openeing the login page");
    }

    @When("The user clicks the registration link")
    public void the_user_clicks_the_registration_link() {
        System.out.println("clicking the registration link");
    }

    /*
        Note the next two steps have the When decorator, but in the feature file they use the
        And gherkin keyword. While it is helpful for us humans to match the Gherkin with the
        decorator keyword, all Cucumber sees is "new step". So even though the keywords do not
        match Cucumber will still run correctly
    */

    @When("The user enters valid crendentials")
    public void the_user_enters_valid_crendentials() {
        System.out.println("Entering valid credentials");
    }

    @When("The user clicks the register button")
    public void the_user_clicks_the_register_button() {
        System.out.println("Clicking register button");
    }

    @Then("The user should see a success message")
    public void the_user_should_see_a_success_message() {
        System.out.println("Success!");
    }

}
