package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.By;

import java.nio.file.Paths;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for browser navigation (back, forward, and page traversal).
 * 
 * Key concepts demonstrated:
 * - driver.get(url): navigates to a URL and waits for the page to load
 * - driver.navigate().back(): moves one step back in browser history (like clicking the back button)
 * - driver.navigate().forward(): moves one step forward in browser history (like clicking the forward button)
 * - driver.navigate().to(url): navigates to a URL (similar to get(), but part of the Navigation interface)
 * 
 * Note: navigate().back() and navigate().forward() depend on browser history.
 * They will only work if there are pages in the history stack to navigate to.
 */
public class NavigationSteps {

    @Given("I am on navigation page 1")
    public void iAmOnNavigationPage1() {
        // Load the first page — this becomes the starting point of our browser history
        String path = Paths.get("src/main/resources/navigation-page1.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @When("I click the link to page 2")
    public void iClickLinkToPage2() {
        // Clicking a link adds a new entry to the browser history stack
        driver.findElement(By.id("link-page2")).click();
    }

    @When("I click the link to page 3")
    public void iClickLinkToPage3() {
        driver.findElement(By.id("link-page3")).click();
    }

    @Then("I should be on page 1")
    public void iShouldBeOnPage1() {
        // Verify the current page by checking the <h1> heading text
        String heading = driver.findElement(By.tagName("h1")).getText();
        assertEquals("Page 1 - Home", heading);
    }

    @Then("I should be on page 2")
    public void iShouldBeOnPage2() {
        String heading = driver.findElement(By.tagName("h1")).getText();
        assertEquals("Page 2 - About", heading);
    }

    @Then("I should be on page 3")
    public void iShouldBeOnPage3() {
        String heading = driver.findElement(By.tagName("h1")).getText();
        assertEquals("Page 3 - Contact", heading);
    }

    @When("I navigate back")
    public void iNavigateBack() {
        // navigate().back() goes to the previous page in the browser history.
        // This is equivalent to pressing the browser's back button.
        driver.navigate().back();
    }

    @When("I navigate forward")
    public void iNavigateForward() {
        // navigate().forward() goes to the next page in the browser history.
        // This only works if you've previously navigated back.
        driver.navigate().forward();
    }
}
