package base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import utils.ExtentReportManager;

public class ApprovalHandling {
	
	private WebDriver driver = DriverManager.getDriver();
	
	public ApprovalHandling(WebDriver driver) {
        this.driver = driver;
    }
	
	public void approveApproval(String recordNo, String approver, ExtentTest test) throws InterruptedException {
	
		// Approving the approval 
        Thread.sleep(2000);
        driver.get(BaseTest.baseUrl +"/sysapproval_approver_list.do");
        Thread.sleep(2000);
        // Search record on table
        WebElement Approvals3=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
        Approvals3.click();
        Thread.sleep(3000);
              
        Select selectValu3=new Select(Approvals3);
        selectValu3.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox23 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        System.out.println("Request approval for :"+recordNo);
        globalSearchBox23.sendKeys(recordNo);
        Thread.sleep(2000);
        globalSearchBox23.sendKeys(Keys.ENTER);
        
			
		Thread.sleep(2000); 
		WebElement approversearch3=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
        approversearch3.sendKeys(approver);
        approversearch3.sendKeys(Keys.ENTER);
		      
        WebElement requestedbutton3=driver.findElement(By.xpath("//*[@class='linked formlink']"));
        requestedbutton3.click();
     
        driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
        Thread.sleep(2000);
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "Approval");
        test.info("Approval Approved", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}
	
	public void RejectApproval(String recordNo, String approver, ExtentTest test) throws InterruptedException {
		
		// Rejecting the approval 
        Thread.sleep(2000);
        driver.get(BaseTest.baseUrl +"/sysapproval_approver_list.do");
        Thread.sleep(2000);
        // Search record on table
        WebElement Approvals3=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
        Approvals3.click();
        Thread.sleep(3000);
              
        Select selectValu3=new Select(Approvals3);
        selectValu3.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox23 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        System.out.println("Request approval for :"+recordNo);
        globalSearchBox23.sendKeys(recordNo);
        Thread.sleep(2000);
        globalSearchBox23.sendKeys(Keys.ENTER);
       
		Thread.sleep(2000); 
		WebElement approversearch3=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
        approversearch3.sendKeys(approver);
        approversearch3.sendKeys(Keys.ENTER);
		      
        WebElement requestedbutton3=driver.findElement(By.xpath("//*[@class='linked formlink']"));
        requestedbutton3.click();
     
        driver.findElement(By.xpath("//*[@id=\"reject\"]")).click();
        Thread.sleep(2000);
 
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "Approval");
        test.info("Rejected the Approval", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}

}
