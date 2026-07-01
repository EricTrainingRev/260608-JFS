package org.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.example.CucumberRunner.driver;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Step definitions demonstrating Selenium's screenshot capability.
 * 
 * Key concepts demonstrated:
 * - TakesScreenshot interface: cast the WebDriver to TakesScreenshot to access screenshot methods
 * - getScreenshotAs(OutputType.FILE): captures the visible viewport and saves it as a temp file
 * - OutputType options:
 *     - OutputType.FILE: returns a temporary File object
 *     - OutputType.BYTES: returns raw byte array (useful for attaching to reports)
 *     - OutputType.BASE64: returns a Base64-encoded string (useful for embedding in HTML reports)
 * - After capturing, you typically copy/move the temp file to a permanent location
 * 
 * Screenshots are saved to the "screenshots" directory at the project root.
 * This is useful for debugging test failures or documenting visual test results.
 */
public class ScreenshotSteps {

    @Given("I am on the screenshot practice page")
    public void iAmOnTheScreenshotPracticePage() {
        String path = Paths.get("src/main/resources/screenshot-practice.html").toAbsolutePath().toUri().toString();
        driver.get(path);
    }

    @Then("the page should display {string}")
    public void thePageShouldDisplay(String expectedImage) {
        // Verify which image is currently shown by reading the label text
        String actual = driver.findElement(By.id("current-image-label")).getText();
        assertEquals("Currently displaying: " + expectedImage, actual);
    }

    @When("I click the button to show image 2")
    public void iClickButtonToShowImage2() {
        driver.findElement(By.id("btn-img2")).click();
    }

    @When("I click the button to show image 3")
    public void iClickButtonToShowImage3() {
        driver.findElement(By.id("btn-img3")).click();
    }

    @When("I take a screenshot named {string}")
    public void iTakeAScreenshotNamed(String fileName) throws IOException {
        // Step 1: Cast the driver to TakesScreenshot.
        // Not all WebDriver implementations support screenshots, but ChromeDriver does.
        TakesScreenshot screenshotDriver = (TakesScreenshot) driver;

        // Step 2: Capture the screenshot as a temporary file.
        // getScreenshotAs(OutputType.FILE) captures the entire visible viewport.
        File tempFile = screenshotDriver.getScreenshotAs(OutputType.FILE);

        // Step 3: Define where to save the screenshot permanently.
        // We create a "screenshots" folder at the project root to store them.
        Path screenshotDir = Paths.get("screenshots");
        Files.createDirectories(screenshotDir);

        // Step 4: Copy the temp file to our target location with a meaningful name.
        Path destination = screenshotDir.resolve(fileName + ".png");
        Files.copy(tempFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Screenshot saved to: " + destination.toAbsolutePath());
    }
}
