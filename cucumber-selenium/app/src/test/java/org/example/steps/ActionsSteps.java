package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.nio.file.Paths;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Step definitions demonstrating Selenium's Actions API for advanced user interactions.
 * 
 * Key concepts demonstrated:
 * - Actions class: provides methods to build complex chains of user interactions
 * - new Actions(driver): creates an Actions builder bound to the current WebDriver
 * - dragAndDrop(source, target): clicks and holds the source element, moves to the target, and releases
 * - perform(): executes the entire chain of actions that have been built
 * 
 * Other useful Actions methods (not all shown here):
 * - clickAndHold(element): presses left mouse button on an element without releasing
 * - moveToElement(element): moves the mouse to the center of an element
 * - release(): releases the left mouse button
 * - doubleClick(element): double-clicks an element
 * - contextClick(element): right-clicks an element
 * - moveByOffset(x, y): moves the mouse by an offset from its current position
 * 
 * The Actions API is essential for testing drag-and-drop interfaces, hover menus,
 * slider controls, and any interaction that requires precise mouse/keyboard control.
 */
public class ActionsSteps {

    @Given("I am on the actions practice page")
    public void iAmOnTheActionsPracticePage() {
        String path = Paths.get("src/main/resources/actions-practice.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @Then("the red box should be visible")
    public void theRedBoxShouldBeVisible() {
        // isDisplayed() returns true if the element is rendered on the page (not hidden via CSS)
        WebElement redBox = driver.findElement(By.id("red-box"));
        assertTrue(redBox.isDisplayed());
    }

    @And("the yellow box should be visible")
    public void theYellowBoxShouldBeVisible() {
        WebElement yellowBox = driver.findElement(By.id("yellow-box"));
        assertTrue(yellowBox.isDisplayed());
    }

    @And("the orange box should not be visible")
    public void theOrangeBoxShouldNotBeVisible() {
        WebElement orangeBox = driver.findElement(By.id("orange-box"));
        assertFalse(orangeBox.isDisplayed());
    }

    @When("I drag the red box onto the yellow box")
    public void iDragTheRedBoxOntoTheYellowBox() {
        // Step 1: Locate the source (red box) and target (yellow box) elements
        WebElement source = driver.findElement(By.id("red-box"));
        WebElement target = driver.findElement(By.id("yellow-box"));

        // Step 2: Create an Actions object. This is the builder for complex interactions.
        Actions actions = new Actions(driver);

        // Step 3: Use dragAndDrop() to perform the full drag sequence:
        //   - Clicks and holds on the source element
        //   - Moves the mouse to the center of the target element
        //   - Releases the mouse button
        // The perform() call at the end executes the entire chain.
        actions.dragAndDrop(source, target).perform();
    }

    @Then("the orange box should be visible")
    public void theOrangeBoxShouldBeVisible() {
        WebElement orangeBox = driver.findElement(By.id("orange-box"));
        assertTrue(orangeBox.isDisplayed());
    }

    @And("the red box should not be visible")
    public void theRedBoxShouldNotBeVisible() {
        WebElement redBox = driver.findElement(By.id("red-box"));
        assertFalse(redBox.isDisplayed());
    }

    @And("the yellow box should not be visible")
    public void theYellowBoxShouldNotBeVisible() {
        WebElement yellowBox = driver.findElement(By.id("yellow-box"));
        assertFalse(yellowBox.isDisplayed());
    }

    @And("the status message should display {string}")
    public void theStatusMessageShouldDisplay(String expected) {
        String actual = driver.findElement(By.id("result-message")).getText();
        assertEquals(expected, actual);
    }
}
