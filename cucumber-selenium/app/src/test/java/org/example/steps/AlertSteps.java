package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import java.nio.file.Paths;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for handling JavaScript alerts, confirms, and prompts.
 * 
 * Key concepts demonstrated:
 * - driver.switchTo().alert(): switches WebDriver's focus to the active browser alert
 * - alert.accept(): clicks "OK" on the alert (works for all alert types)
 * - alert.dismiss(): clicks "Cancel" on a confirm or prompt dialog
 * - alert.sendKeys(): types text into a prompt dialog's input field
 * - After interacting with an alert, focus automatically returns to the page
 * 
 * Important: You must switch to the alert before interacting with it.
 * Attempting to interact with page elements while an alert is open will throw
 * an UnhandledAlertException.
 */
public class AlertSteps {

    @Given("I am on the alert practice page")
    public void iAmOnTheAlertPracticePage() {
        String path = Paths.get("src/main/resources/alert-practice.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @When("I click the simple alert button")
    public void iClickTheSimpleAlertButton() {
        // Clicking this button triggers a JavaScript alert() call
        driver.findElement(By.id("simple-alert-btn")).click();
    }

    @When("I click the confirm alert button")
    public void iClickTheConfirmAlertButton() {
        // Clicking this button triggers a JavaScript confirm() call
        driver.findElement(By.id("confirm-alert-btn")).click();
    }

    @When("I click the prompt alert button")
    public void iClickThePromptAlertButton() {
        // Clicking this button triggers a JavaScript prompt() call
        driver.findElement(By.id("prompt-alert-btn")).click();
    }

    @And("I accept the alert")
    public void iAcceptTheAlert() {
        // switchTo().alert() returns an Alert object representing the currently open alert.
        // accept() is equivalent to clicking "OK" on the dialog.
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    @And("I dismiss the alert")
    public void iDismissTheAlert() {
        // dismiss() is equivalent to clicking "Cancel" on a confirm or prompt dialog.
        // On a simple alert, dismiss() behaves the same as accept().
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
    }

    @And("I type {string} into the alert and accept")
    public void iTypeIntoTheAlertAndAccept(String text) {
        // For prompt dialogs, sendKeys() types into the prompt's text input field.
        // You must call sendKeys before accept/dismiss, as accepting closes the alert.
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        alert.accept();
    }

    @Then("the simple alert result should display {string}")
    public void theSimpleAlertResultShouldDisplay(String expected) {
        // After the alert is dismissed, focus returns to the page and we can read elements
        String actual = driver.findElement(By.id("simple-alert-result")).getText();
        assertEquals(expected, actual);
    }

    @Then("the confirm alert result should display {string}")
    public void theConfirmAlertResultShouldDisplay(String expected) {
        String actual = driver.findElement(By.id("confirm-alert-result")).getText();
        assertEquals(expected, actual);
    }

    @Then("the prompt alert result should display {string}")
    public void thePromptAlertResultShouldDisplay(String expected) {
        String actual = driver.findElement(By.id("prompt-alert-result")).getText();
        assertEquals(expected, actual);
    }
}
