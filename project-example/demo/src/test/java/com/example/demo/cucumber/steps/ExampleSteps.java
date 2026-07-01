package com.example.demo.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.cucumber.CucumberRunner;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExampleSteps {

  // this will store the driver object
  private final WebDriver driver;

  // at test time the CucumberRunner will be injected into the constructor so we can get access to
  // the driver and any future test resources
  public ExampleSteps(CucumberRunner runner) {
    driver = runner.getDriver();
  }

  @Given("The user is on the login page")
  public void the_user_is_on_the_login_page() {
    // use the "get" method to open a webpage
    driver.get("http://localhost:4200");
  }

  @When("The user clicks the registration link")
  public void the_user_clicks_the_registration_link() {
    // we need to first tell Selenium how to find the element we want to interact with
    // we do this by using the findElement method and providing a "By" object. This object
    // tells the driver what the locator strategy is
    WebElement registrationLink = driver.findElement(By.linkText("Register here"));

    // now that we have a Java representation of our element we need to tell the driver what to
    // do with that element. In our case we want to click it, so we use the "click" method
    registrationLink.click();
  }

  /*
      Note the next two steps have the When decorator, but in the feature file they use the
      And gherkin keyword. While it is helpful for us humans to match the Gherkin with the
      decorator keyword, all Cucumber sees is "new step". So even though the keywords do not
      match Cucumber will still run correctly
  */

  @When("The user enters valid crendentials")
  public void the_user_enters_valid_crendentials() {
    // note we can chain the locating and acting in a single statement
    // sendKeys is used to type into an element
    driver.findElement(By.id("username")).sendKeys("Username");
    driver.findElement(By.id("password")).sendKeys("P0ssword");
  }

  @When("The user clicks the register button")
  public void the_user_clicks_the_register_button() {
    driver.findElement(By.tagName("button")).click();
  }

  @Then("The user should see a success message")
  public void the_user_should_see_a_success_message() {
    // if you have no good options for your By selectors you can always use cssSelector
    WebElement statusMessage = driver.findElement(By.cssSelector("p[role='status']"));
    assertEquals("Registration successful!", statusMessage.getText());
  }
}
