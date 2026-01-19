package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Config;

public class ChangePage {
	
	private WebDriver driver;
    public WebDriverWait wait;
    private static JavascriptExecutor jse;
	
	public ChangePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // ✅ Explicit wait
        this.jse = (JavascriptExecutor) driver;
    }

	//Create Change Navigation
	
	
	//Open record from Change list
	public void openChangeRecord(String changeNo) throws InterruptedException {
		driver.get(Config.baseUrl() + "/change_request_list");
        Thread.sleep(2000);
        // Search Change record on table
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(changeNo);
        Thread.sleep(1000);
        globalSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        // Open Change
        List<WebElement> openCHN = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]/a"));
        for (WebElement ele2 : openCHN) {
             String currentINC = ele2.getText();
             if (currentINC.contains(changeNo)) 
             {
                 ele2.click();
                 break;
             }
        }
	}
}
