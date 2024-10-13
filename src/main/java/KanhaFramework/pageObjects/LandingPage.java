package KanhaFramework.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import KanhaFramework.AbstractComponents.AbstractComponent;

public class LandingPage extends AbstractComponent {
	WebDriver driver;

	public LandingPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	// driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys("kanhu@gmail.com");

	@FindBy(xpath = "//input[@id='userEmail']")
	WebElement userEmail;

	@FindBy(xpath = "//input[@id='userPassword']")
	WebElement userPass;

	@FindBy(xpath = "//input[@id='login']")
	WebElement submit;

	@FindBy(css = "[class*='flyInOut']")
	WebElement errorMessage;

	public ProductCatalogue login(String email, String password) {
		userEmail.sendKeys(email);
		userPass.sendKeys(password);
		submit.click();
		ProductCatalogue pc = new ProductCatalogue(driver);
		return pc;
	}

	public void goTo() {
		String url = "https://rahulshettyacademy.com/client/";
		driver.get(url);
	}

	public String getErrorMessage() {

		waitForAppear(errorMessage);
		return errorMessage.getText();
	}

}
