package KanhaFrameworkDesign.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAloneTest {
	public static void main(String[] args) throws InterruptedException {
		String productName = "ZARA COAT 3";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		String url = "https://rahulshettyacademy.com/client/";
		driver.get(url);
		driver.findElement(By.xpath("//input[@id='userEmail']")).sendKeys("kanhu@gmail.com");
		driver.findElement(By.xpath("//input[@id='userPassword']")).sendKeys("Kanha@6173");
		driver.findElement(By.xpath("//input[@id='login']")).click();
		String expectedDashboardUrl = "https://rahulshettyacademy.com/client/dashboard/dash";
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe(expectedDashboardUrl));

		if (driver.getCurrentUrl().equals(expectedDashboardUrl)) {
			System.out.println("Login Sucessufully");
		} else {
			System.out.println("Unable to loging");
		}

		// driver.findElements(By.cssSelector(".mb-3"));
		List<WebElement> products = driver.findElements(By.xpath("//div[@class='card']"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));
		WebElement prod = products.stream()
				.filter(product -> product.findElement(By.cssSelector("b")).getText().equals(productName)).findFirst()
				.orElse(null);
		if (prod != null) {

			prod.findElement(By.cssSelector(".btn.w-10.rounded")).click();
			System.out.println("Product added to cart successfully");
		} else {
			System.out.println("Product not found");
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));
		wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(".ng-animating"))));
		driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();
		// "[]routerlink*=''cart"

		List<WebElement> cartProducts = driver.findElements(By.xpath("//div[@class='cartSection']/h3"));
		Boolean match = cartProducts.stream()
				.anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
		Assert.assertTrue(match);
		driver.findElement(By.cssSelector(".totalRow button")).click();
		WebElement creditCard = driver.findElement(By.xpath("(//input[@class='input txt text-validated'])[1]"));
		Actions action = new Actions(driver);
		action.click(creditCard).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE)
				.sendKeys("7894 5612 3214 1245").perform();

		// Select Monthdropdown
		WebElement monthdropdown = driver.findElement(By.xpath("(//select[@class='input ddl']) [1]"));
		Select select = new Select(monthdropdown);
		select.selectByVisibleText("03");

		// Select daydropdown
		WebElement daydropdown = driver.findElement(By.xpath("(//select[@class='input ddl']) [2]"));
		Select selected = new Select(daydropdown);
		selected.selectByVisibleText("29");

		// add cvv
		driver.findElement(By.xpath("(//input[@class='input txt'])[1]")).sendKeys("556");

		// Name on Card
		driver.findElement(By.xpath("(//input[@class='input txt'])[2]")).sendKeys("KANHU CHARAN GHADAI");

		// Select country
		action.sendKeys(driver.findElement(By.xpath("//input[@placeholder='Select Country']")), "India").build()
				.perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));

		driver.findElement(By.xpath("(//button[@class='ta-item list-group-item ng-star-inserted'])[2]")).click();

		// Click on Place Order
		driver.findElement(By.xpath("//a[@class='btnn action__submit ng-star-inserted']")).click();

		// Confirmation message
		String confirmationMessage = driver.findElement(By.xpath("//h1[@class='hero-primary']")).getText();
		System.out.println(confirmationMessage);
		Assert.assertTrue(confirmationMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER."));
		Thread.sleep(5000);
		driver.close();
	}

}
