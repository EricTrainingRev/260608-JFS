package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.nio.file.Paths;
import java.util.List;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions demonstrating XPath locator strategies in Selenium.
 * 
 * Key concepts demonstrated:
 * - By.xpath("//tag[@attribute='value']"): locate elements by any attribute (id, name, placeholder, etc.)
 * - XPath indexing: //table/tbody/tr[2] selects the second row (1-based index)
 * - Relative XPath (./): search within the context of a previously found element
 * - contains(text(), 'value'): partial text matching within element content
 * - getAttribute("value"): retrieves the current value of an input field
 * 
 * XPath syntax reference:
 * - // : selects nodes anywhere in the document (relative)
 * - /  : selects from the root or direct child (absolute)
 * - ./ : selects relative to the current node context
 * - @  : selects an attribute
 * - [] : applies a predicate/filter condition
 * - contains(@attr, 'text') : partial attribute matching
 * - text() : references the text content of an element
 */
public class XpathSteps {

    private WebElement locatedRow;
    private List<WebElement> matchingNotifications;

    @Given("I am on the xpath practice page")
    public void iAmOnTheXpathPracticePage() {
        String path = Paths.get("src/main/resources/xpath-practice.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @When("I enter {string} into the first name field using XPath by id")
    public void iEnterIntoFirstNameByXpathId(String text) {
        // XPath by id attribute: //input[@id='first-name']
        // The // means search anywhere in the document, input is the tag name,
        // and [@id='first-name'] filters to the element with that specific id.
        WebElement field = driver.findElement(By.xpath("//input[@id='first-name']"));
        field.sendKeys(text);
    }

    @And("I enter {string} into the last name field using XPath by name")
    public void iEnterIntoLastNameByXpathName(String text) {
        // XPath by name attribute: //input[@name='lastName']
        // Useful when elements have a name attribute but no unique id.
        WebElement field = driver.findElement(By.xpath("//input[@name='lastName']"));
        field.sendKeys(text);
    }

    @And("I enter {string} into the email field using XPath by placeholder")
    public void iEnterIntoEmailByXpathPlaceholder(String text) {
        // XPath by placeholder attribute: //input[@placeholder='Enter email']
        // Any HTML attribute can be used in an XPath predicate.
        WebElement field = driver.findElement(By.xpath("//input[@placeholder='Enter email']"));
        field.sendKeys(text);
    }

    @Then("the first name field should contain {string}")
    public void theFirstNameFieldShouldContain(String expected) {
        // getAttribute("value") retrieves the current text typed into an input field.
        // Note: getText() does NOT work for input fields — it returns empty string.
        String actual = driver.findElement(By.xpath("//input[@id='first-name']")).getAttribute("value");
        assertEquals(expected, actual);
    }

    @And("the last name field should contain {string}")
    public void theLastNameFieldShouldContain(String expected) {
        String actual = driver.findElement(By.xpath("//input[@name='lastName']")).getAttribute("value");
        assertEquals(expected, actual);
    }

    @When("I locate the second row of the employee table using XPath indexing")
    public void iLocateSecondRowUsingXpathIndexing() {
        // XPath indexing: tr[2] selects the second <tr> element (1-based, not 0-based!)
        // The full path: find the table by id -> go to tbody -> get the 2nd row
        locatedRow = driver.findElement(By.xpath("//table[@id='employee-table']/tbody/tr[2]"));
    }

    @Then("the employee name in that row should be {string}")
    public void theEmployeeNameShouldBe(String expected) {
        // Relative XPath with ./: searches within the context of locatedRow only.
        // Without the dot, it would search the entire document.
        String actual = locatedRow.findElement(By.xpath("./td[@class='emp-name']")).getText();
        assertEquals(expected, actual);
    }

    @When("I locate all notifications that contain the text {string} using XPath contains")
    public void iLocateNotificationsContainingText(String text) {
        // contains(text(), 'value') performs partial text matching.
        // findElements (plural) returns a List of all matching elements.
        // If no matches are found, it returns an empty list (does NOT throw an exception).
        matchingNotifications = driver.findElements(By.xpath("//li[contains(text(),'" + text + "')]"));
    }

    @Then("I should find {int} matching notification")
    public void iShouldFindMatchingNotifications(int count) {
        assertEquals(count, matchingNotifications.size());
    }
}
