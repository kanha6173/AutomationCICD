package KanhaFrameworkDesign.tests;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import KanhaFramework.pageObjects.CartPage;
import KanhaFramework.pageObjects.CheckOutPage;
import KanhaFramework.pageObjects.ConfirmationPage;
import KanhaFramework.pageObjects.OrderPage;
import KanhaFramework.pageObjects.ProductCatalogue;
import KanhaFrameworkDesign.TestComponent.BaseTest;

public class SampleTest extends BaseTest {

	@Test(dataProvider = "getData", groups = { "Purchase" })
	public void submitOrder(HashMap<String, String> input) throws InterruptedException, IOException {
		// Login to the application
		ProductCatalogue pc = lp.login(input.get("email"), input.get("password"));

		// Add product to cart
		pc.addProductToCart(input.get("productName"));
		CartPage cp = pc.goToCartPage();

		// Verify the product is displayed in the cart
		Boolean match = cp.verifyProductDisplay(input.get("productName"));
		Assert.assertTrue(match);

		// Go to checkout page
		CheckOutPage checkout = cp.goTocheckOut();
		checkout.fillCreditCard();
		checkout.fillMonth();
		checkout.filldate(); // Call once
		checkout.fillCvv();
		checkout.selectCountry("India");

		// Create a WebDriverWait instance
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Wait for any loading spinner to disappear
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));

		// Wait for the submit button to be clickable
		WebElement submitButton = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//a[@class='btnn action__submit ng-star-inserted']")));

		// Scroll to the element
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		// Optional: Wait again to ensure that the overlay is no longer present
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));

		// Try to click the button
		try {
			submitButton.click();
		} catch (ElementClickInterceptedException e) {
			// If intercepted, wait again for overlay to disappear and then retry
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));
			submitButton.click(); // Retry clicking the button
		}

		// After submitting the order, get the confirmation message
		ConfirmationPage confirmationPage = new ConfirmationPage(driver);
		String confirmationMessage = confirmationPage.getConfirmatonMessage();

		// Validate the confirmation message
		Assert.assertTrue(confirmationMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
		Thread.sleep(5000);
	}

	@Test(dependsOnMethods = { "submitOrder" }, dataProvider = "getData")
	public void orderHistory(String email, String password, String productName) {
		// Login to the application
		ProductCatalogue pc = lp.login(email, password);
		OrderPage op = pc.gotoOrdersPage();

		// Verify that the order is displayed in the order history
		Assert.assertTrue(op.verifyOrderDisplay(productName));
	}

	@DataProvider
	public Object[][] getData() throws IOException {
		// Load data from JSON file
		List<HashMap<String, String>> data = getJsonDataToMap(
				System.getProperty("user.dir") + "\\src\\test\\java\\KanhaFrameworkDesign\\data\\PurchaseOrder.json");
		return new Object[][] { { data.get(0) }, { data.get(1) } };
	}
}
