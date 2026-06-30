package com.example.demo.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite //This tells Junit the class facilitates other test classes
@IncludeEngines("cucumber")//This tells Junit to let Cucumber facilitate the tests associated with this class
@SelectPackages({"features","com.example.demo.cucumber.steps"})//This tells junit to include the features directory of "resources" and the steps package as part of the suite
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.demo.cucumber.steps")//This tells CUCUMBER where the code associated with the acceptance criteria is located
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:reports/cucumber-report.html")//This tells Cucumber to create an html test report
public class CucumberRunner {
    
}
