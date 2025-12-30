package base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ApprovalHandling {
	
	private static WebDriver driver = DriverManager.getDriver();
	
	public ApprovalHandling(WebDriver driver) {
        this.driver = driver;
    }
	
	public static void approveApproval(String value) throws InterruptedException {
	
		driver.get(BaseTest.baseUrl + "sysapproval_approver_list");
		Thread.sleep(2000);
		// Search Change record on table
		WebElement Approvals=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
		Approvals.click();
		Thread.sleep(3000);
		
		Select selectValu=new Select(Approvals);
		selectValu.selectByVisibleText("Approval for");
		
		Thread.sleep(2000);
		WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox2.sendKeys(value);
		Thread.sleep(2000);
		globalSearchBox2.sendKeys(Keys.ENTER);
		
		WebElement approversearch=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
		approversearch.sendKeys("Manifah Masood");
		approversearch.sendKeys(Keys.ENTER);
		
		
		WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
		requestedbutton.click();
		
		driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
		Thread.sleep(2000);
	}
	
	public static void RejectApproval(String value) throws InterruptedException {
		
		driver.get(BaseTest.baseUrl + "sysapproval_approver_list");
		Thread.sleep(2000);
		// Search Change record on table
		WebElement Approvals=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
		Approvals.click();
		Thread.sleep(3000);
		
		Select selectValu=new Select(Approvals);
		selectValu.selectByVisibleText("Approval for");
		
		Thread.sleep(2000);
		WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox2.sendKeys(value);
		Thread.sleep(2000);
		globalSearchBox2.sendKeys(Keys.ENTER);
		
		WebElement approversearch=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
		approversearch.sendKeys("Manifah Masood");
		approversearch.sendKeys(Keys.ENTER);
		
		
		WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
		requestedbutton.click();
		
		driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
		Thread.sleep(2000);
	}

}
