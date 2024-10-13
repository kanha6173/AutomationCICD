package Cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/Cucumber/PurchaseOrder.feature", 
		glue = "KanhaFrameworkDesign.stepDefinitions", 
		monochrome = true, plugin = { "html:target/cucumber.html" }
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {
}
