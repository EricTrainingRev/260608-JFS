package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Paths;
import java.time.Duration;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Step definitions demonstrating Selenium's explicit waits (WebDriverWait).
 * 
 * Key concepts demonstrated:
 * - WebDriverWait: polls the DOM at regular intervals until a condition is met or timeout expires
 * - ExpectedConditions.visibilityOfElementLocated(): waits until an element is present AND visible
 * - ExpectedConditions.elementToBeClickable(): waits until an element is visible AND enabled
 * - ExpectedConditions.textToBe(): waits until an element's text matches the expected value
 * - ExpectedConditions.invisibilityOfElementLocated(): waits until an element is no longer visible
 * 
 * Explicit waits are preferred over Thread.sleep() because they:
 * 1. Only wait as long as needed (faster tests)
 * 2. Have a maximum timeout (no infinite hangs)
 * 3. Clearly express WHAT you're waiting for (self-documenting)
 * 
 * They differ from implicit waits in that they target specific conditions
 * rather than applying a blanket wait to all findElement calls.
 */
public class ExplicitWaitSteps {

    @Given("I am on the explicit wait practice page")
    public void iAmOnTheExplicitWaitPracticePage() {
        String path = Paths.get("src/main/resources/explicit-wait-practice.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @When("I click the load content button")
    public void iClickTheLoadContentButton() {
        driver.findElement(By.id("load-content-btn")).click();
    }

    @Then("I should see the delayed content within {int} seconds")
    public void iShouldSeeDelayedContent(int seconds) {
        // WebDriverWait takes the driver and a Duration as the max wait time.
        // visibilityOfElementLocated waits for the element to be present in the DOM
        // AND have a height/width greater than 0 (i.e., not hidden via CSS).
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        WebElement content = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.id("delayed-content"))
        );
        assertTrue(content.isDisplayed());
    }

    @When("I click the enable submit button")
    public void iClickTheEnableSubmitButton() {
        driver.findElement(By.id("enable-btn")).click();
    }

    @Then("the submit button should become clickable within {int} seconds")
    public void theSubmitButtonShouldBecomeClickable(int seconds) {
        // elementToBeClickable waits until the element is both visible AND enabled.
        // A disabled button (disabled attribute) will not satisfy this condition.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delayed-submit")));
    }

    @When("I click the submit button")
    public void iClickTheSubmitButton() {
        driver.findElement(By.id("delayed-submit")).click();
    }

    @Then("I should see the confirmation message")
    public void iShouldSeeTheConfirmationMessage() {
        WebElement confirmation = driver.findElement(By.id("submit-confirmation"));
        assertTrue(confirmation.isDisplayed());
    }

    @When("I click the update status button")
    public void iClickTheUpdateStatusButton() {
        driver.findElement(By.id("update-status-btn")).click();
    }

    @Then("the status text should change to {string} within {int} seconds")
    public void theStatusTextShouldChangeTo(String expected, int seconds) {
        // textToBe waits until the element's getText() matches the expected string exactly.
        // Useful when JavaScript updates text content asynchronously.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.textToBe(By.id("status-text"), expected));
    }

    @When("I click the dismiss banner button")
    public void iClickTheDismissBannerButton() {
        driver.findElement(By.id("dismiss-btn")).click();
    }

    @Then("the banner should disappear within {int} seconds")
    public void theBannerShouldDisappear(int seconds) {
        // invisibilityOfElementLocated waits until the element is either:
        // - No longer present in the DOM, OR
        // - Present but not visible (display:none, visibility:hidden, height/width 0)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("banner")));
    }
}
