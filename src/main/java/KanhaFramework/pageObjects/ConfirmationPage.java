package KanhaFramework.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import KanhaFramework.AbstractComponents.AbstractComponent;

public class ConfirmationPage extends AbstractComponent {
	WebDriver driver;

	public ConfirmationPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
	}

	@FindBy(xpath = "//h1[@class='hero-primary']")
	WebElement confirmationMessage;

	public String getConfirmatonMessage() {
		return confirmationMessage.getText();
	}

}
