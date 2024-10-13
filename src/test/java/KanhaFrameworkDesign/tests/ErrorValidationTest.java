package KanhaFrameworkDesign.tests;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.sun.net.httpserver.Authenticator.Retry;

import KanhaFramework.pageObjects.CartPage;
import KanhaFramework.pageObjects.CheckOutPage;
import KanhaFramework.pageObjects.ConfirmationPage;
import KanhaFramework.pageObjects.ProductCatalogue;
import KanhaFrameworkDesign.TestComponent.BaseTest;
import KanhaFrameworkDesign.TestComponent.Retry;

public class ErrorValidationTest extends BaseTest {
	String productName = "ZARA COAT 3";

	@Test(groups = { "ErrorHandling" }, retryAnalyzer = Retry.class)
	public void loginErrorValidation() {

		lp.login("kanhu@gmail.com", "Kanh6173");
		String actualErrorMessage = lp.getErrorMessage();
		Assert.assertEquals("Incorrect email or password.", actualErrorMessage);
	}

	@Test
	public void productErrorValidation() throws InterruptedException {

		ProductCatalogue pc = lp.login("kanhu@gmail.com", "Kanha@6173");

		List<WebElement> products = pc.getProductList();
		pc.addProductToCart(productName);
		CartPage cp = pc.goToCartPage();

		Boolean match = cp.verifyProductDisplay("ZARA COAT 33");
		Assert.assertFalse(match);

	}

}
