package KanhaFramework.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import KanhaFramework.AbstractComponents.AbstractComponent;

public class CheckOutPage extends AbstractComponent {
	WebDriver driver;

	public CheckOutPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "(//input[@class='input txt text-validated'])[1]")
	WebElement creditCard;

	@FindBy(xpath = "(//select[@class='input ddl']) [1]")
	WebElement monthdropdown;

	@FindBy(xpath = "(//select[@class='input ddl']) [2]")
	WebElement daydropdown;

	@FindBy(xpath = "(//input[@class='input txt'])[1]")
	WebElement cvv;

	@FindBy(xpath = "(//input[@class='input txt'])[2]")
	WebElement name;

	@FindBy(xpath = "//input[@placeholder='Select Country']")

	WebElement country;

	@FindBy(xpath = "(//button[@class='ta-item list-group-item ng-star-inserted'])[2]")
	WebElement selectCountry;

	@FindBy(xpath = "//a[@class='btnn action__submit ng-star-inserted']")
	WebElement submit;

	By results = By.cssSelector(".ta-results");

	public void fillCreditCard() {
		/*
		 * Actions action = new Actions(driver);
		 * 
		 * // action.click(creditCard).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.
		 * CONTROL).sendKeys(Keys.BACK_SPACE)
		 * .sendKeys("7894 5612 3214 1245").perform();
		 * 
		 */
		waitForElementToAppear(By.xpath("(//input[@class='input txt text-validated'])[1]")); // Using By locator
		creditCard.clear(); // Ensure the field is clear
		creditCard.sendKeys("7894 5612 3214 1245");

	}

	public void fillMonth() {
		Select select = new Select(monthdropdown);
		select.selectByVisibleText("03");
	}

	public void filldate() {
		Select selected = new Select(daydropdown);
		selected.selectByVisibleText("29");
	}

	public void fillCvv() {
		cvv.sendKeys("556");
	}

	public void fillName() {
		name.sendKeys("KANHU CHARAN GHADAI");
	}

	public void selectCountry(String countryName) {
		Actions action = new Actions(driver);
		action.sendKeys((country), countryName).build().perform();
		waitForElementToAppear(results);
		selectCountry.click();
	}

	public ConfirmationPage submitOrder() {
		submit.click();
		return new ConfirmationPage(driver);
	}

}
