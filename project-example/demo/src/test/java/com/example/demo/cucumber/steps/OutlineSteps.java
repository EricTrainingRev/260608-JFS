package com.example.demo.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.cucumber.CucumberRunner;
import com.example.demo.cucumber.poms.RegistrationPom;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class OutlineSteps {

  private RegistrationPom registrationPom;

  public OutlineSteps(CucumberRunner runner) {
    registrationPom = runner.getRegistrationPom();
  }

  @When("The user enters username {string} and password {string}")
  public void the_user_enters_username_and_password(String username, String password) {
    registrationPom.enterCredentials(username, password);
  }

  @Then("The user should see failure message {string}")
  public void the_user_should_see_failure_message(String message) {
    assertEquals(message, registrationPom.getStatusMessage());
  }
}
