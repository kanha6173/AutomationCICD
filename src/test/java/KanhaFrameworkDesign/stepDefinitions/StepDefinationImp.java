package KanhaFrameworkDesign.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import KanhaFramework.pageObjects.CartPage;
import KanhaFramework.pageObjects.CheckOutPage;
import KanhaFramework.pageObjects.ConfirmationPage;
import KanhaFramework.pageObjects.LandingPage;
import KanhaFramework.pageObjects.ProductCatalogue;
import KanhaFrameworkDesign.TestComponent.BaseTest;

public class StepDefinationImp extends BaseTest {

	public LandingPage landingPage;
	public ProductCatalogue pc;
	public ConfirmationPage confirmationPage;

	@Given("I land on Ecommerce page")
	public void i_land_on_Ecommerce_page() throws IOException {
		landingPage = launchApplication();
		if (landingPage == null) {
			throw new IllegalStateException("Landing Page was not initialized properly!");
		}
		System.out.println("Landing Page initialized: " + (landingPage != null));
	}

	@Given("^Logged in through username (.+) and password (.+)$")
	public void login_through_username_and_password(String username, String password) {
		if (landingPage == null) {
			throw new IllegalStateException("Landing Page is not initialized. Cannot log in.");
		}
		pc = landingPage.login(username, password);
		if (pc == null) {
			throw new IllegalStateException("Product Catalogue was not initialized properly after login!");
		}
	}

	@When("^I add product (.+) to cart$")
	public void i_add_product_to_cart(String productName) {
		if (pc == null) {
			throw new IllegalStateException("Product Catalogue is not initialized. Cannot add product to cart.");
		}
		pc.addProductToCart(productName);
	}

	@When("^I check for the product (.+) in cart$")
	public void i_check_for_the_product_in_cart(String productName) {
		CartPage cp = pc.goToCartPage(); // Ensure CartPage is navigated correctly
		boolean isProductDisplayed = cp.verifyProductDisplay(productName);
		Assert.assertTrue(isProductDisplayed, "Product '" + productName + "' not found in the cart");
	}

	@When("^Checkout (.+) and submit the order$")
	public void checkout_and_submit_the_order(String productName) {
		if (pc == null) {
			throw new IllegalStateException("Product Catalogue is not initialized. Cannot checkout.");
		}

		CartPage cp = pc.goToCartPage();
		boolean match = cp.verifyProductDisplay(productName);
		Assert.assertTrue(match, "Product not found in the cart");

		CheckOutPage checkout = cp.goTocheckOut();
		checkout.fillCreditCard();
		checkout.fillMonth();
		checkout.filldate();
		checkout.fillCvv();
		checkout.selectCountry("India");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));

		WebElement submitButton = wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//a[@class='btnn action__submit ng-star-inserted']")));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));

		try {
			submitButton.click();
		} catch (ElementClickInterceptedException e) {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("ngx-spinner-overlay")));
			submitButton.click();
		}

		// Initialize ConfirmationPage after submitting the order
		confirmationPage = new ConfirmationPage(driver);
	}

	@Then("^I verify the success in step$")
	public void i_verify_the_success_in_step() {
		// Verifying success message after order is placed
		if (confirmationPage == null) {
			throw new IllegalStateException("Confirmation Page is not initialized.");
		}

		String confirmationMessage = confirmationPage.getConfirmatonMessage();
		Assert.assertTrue(confirmationMessage.contains("Success"),
				"Order submission failed or success message not found.");
	}

	@Then("{string} message is displayed on confirmation Page")
	public void message_is_displayed_on_confirmation_Page(String expectedMessage) {
		if (confirmationPage == null) {
			throw new IllegalStateException("Confirmation Page is not initialized.");
		}

		String confirmationMessage = confirmationPage.getConfirmatonMessage();
		Assert.assertTrue(confirmationMessage.equalsIgnoreCase(expectedMessage),
				"Expected message was not displayed on the confirmation page.");
	}
}
