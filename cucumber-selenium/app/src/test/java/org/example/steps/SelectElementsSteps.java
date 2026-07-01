package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.example.CucumberRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions for interacting with HTML <select> elements using Selenium's Select class.
 * 
 * Key concepts demonstrated:
 * - Wrapping a WebElement in a Select object to access dropdown-specific methods
 * - selectByVisibleText(): selects an option by its displayed text
 * - selectByIndex(): selects an option by its position (0-based index)
 * - selectByValue(): selects an option by its "value" attribute
 * - Multi-select support: selecting multiple options in a <select multiple> element
 * - deselectAll(): clears all selections in a multi-select dropdown
 */
public class SelectElementsSteps {

    private CucumberRunner runner = new CucumberRunner();

    @Before("@select")
    public void setup() {
        runner.setup();
    }

    @After("@select")
    public void teardown() {
        runner.teardown();
    }

    @Given("I am on the select practice page")
    public void iAmOnTheSelectPracticePage() {
        // Convert the relative file path to an absolute file:// URI so the browser can load it
        String path = Paths.get("src/main/resources/select-practice.html").toAbsolutePath().toUri().toString();
        runner.driver.get(path);
    }

    @When("I select {string} from the single select dropdown by visible text")
    public void iSelectFromSingleSelectByVisibleText(String text) {
        // Locate the <select> element and wrap it in Selenium's Select class
        WebElement dropdown = runner.driver.findElement(By.id("single-select"));
        Select select = new Select(dropdown);
        // selectByVisibleText matches the text displayed to the user in the dropdown
        select.selectByVisibleText(text);
    }

    @Then("the selected language should display {string}")
    public void theSelectedLanguageShouldDisplay(String expected) {
        String actual = runner.driver.findElement(By.id("selected-language")).getText();
        assertEquals("Selected language: " + expected, actual);
    }

    @When("I select the option at index {int} from the single select dropdown")
    public void iSelectOptionAtIndex(int index) {
        WebElement dropdown = runner.driver.findElement(By.id("single-select"));
        Select select = new Select(dropdown);
        // selectByIndex uses the 0-based position of the <option> elements
        // Index 0 = "--Select--", Index 1 = "Java", Index 2 = "Python", etc.
        select.selectByIndex(index);
    }

    @When("I select {string} from the single select dropdown by value")
    public void iSelectFromSingleSelectByValue(String value) {
        WebElement dropdown = runner.driver.findElement(By.id("single-select"));
        Select select = new Select(dropdown);
        // selectByValue matches the "value" attribute of the <option> tag,
        // e.g., <option value="javascript">JavaScript</option>
        select.selectByValue(value);
    }

    @When("I select {string} and {string} from the multi select")
    public void iSelectMultipleFromMultiSelect(String option1, String option2) {
        // For a <select multiple> element, calling select methods multiple times
        // adds to the selection rather than replacing it
        WebElement multiSelect = runner.driver.findElement(By.id("multi-select"));
        Select select = new Select(multiSelect);
        select.selectByVisibleText(option1);
        select.selectByVisibleText(option2);
    }

    @Then("the selected skills should display {string}")
    public void theSelectedSkillsShouldDisplay(String expected) {
        String actual = runner.driver.findElement(By.id("selected-skills")).getText();
        assertEquals("Selected skills: " + expected, actual);
    }

    @When("I deselect all options from the multi select")
    public void iDeselectAllFromMultiSelect() {
        WebElement multiSelect = runner.driver.findElement(By.id("multi-select"));
        Select select = new Select(multiSelect);
        // deselectAll() clears all selected options — only works on multi-select elements.
        // Calling this on a single-select will throw an UnsupportedOperationException.
        select.deselectAll();
    }
}
