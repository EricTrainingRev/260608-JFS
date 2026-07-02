package org.example;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.cucumber.java.After;
import io.cucumber.java.Before;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import java.time.Duration;

@Suite
@IncludeEngines("cucumber")
@SelectPackages({"features","org.example"})
@ConfigurationParameter(key = GLUE_PROPERTY_NAME,value = "org.example")
public class CucumberRunner {
    
    public static WebDriver driver;

    @Before
    public void setup(){
        /*
            Use Options classes when you need to provide startup instructions and configurations for
            the browser when the Driver spins up a session. Headless is a good one to memorize since
            running your tests in headless mode, especially when you have a multitude of tests, will
            speed them up significantly
        */
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        options.addArguments("--start-maximized");
        // make sure to pass your options as an argument to your driver's constructor
        driver = new ChromeDriver(options);
        // this is a good option to prevent flakey tests, it is NOT a good option for accomodating intentionally long-waited actions
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    @After
    public void teardown(){
        if (driver != null) driver.quit();
        driver = null;
    }

}
