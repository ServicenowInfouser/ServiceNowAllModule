package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import utils.ExtentReportManager;

public class RequestRITMTask extends BaseTest {
	
	public WebDriver driver;
	
	String ritm, approver;
	
	public RequestRITMTask(WebDriver driver) {
        this.driver = driver;
    }
	
	public String CheckReqRITM(ExtentTest test, String requestno) throws InterruptedException {
		
		test = ExtentReportManager.createTest("Verification of Submitted Request and RITM");
		
		//Navigating to Request List
		test.info("Navigating to Request List view");
		driver.get(BaseTest.baseUrl + "/sc_request_list");
		System.out.println(requestno);
		Thread.sleep(2000);
		        
		// Search Request
		test.info("Searching for the created Request " + requestno);
		        //WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @typ='search']"));
		        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		        globalSearchBox.sendKeys(requestno + Keys.ENTER);
		        String screenshotPath1 = ExtentReportManager.captureScreenshot(driver, "list");
		    	test.info("Search generated request", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
		        
		        Thread.sleep(2000);
		    
		        // Open Request
		        test.info("Open Created request");
		        List<WebElement> openReq = driver.findElements(By.xpath("//table[@id='sc_request_table']/tbody/tr/td[3]/a"));
		        for (WebElement ele2 : openReq) {
		            String currentReq = ele2.getText();
		            if (currentReq.contains(requestno)) {
		                ele2.click();
		                break;
		            }
		        }
		        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "Req");
		    	test.info("Navigating to Request", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
		        
		        // Attach custom test data to report
		        //Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
		        System.out.println(driver.getTitle());
		        Thread.sleep(2000);

				//Open RITM
				test.info("Open RITM from the Requested Item Related List");
				ritm = driver.findElement(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a")).getText();
		        List<WebElement> openRITM = driver.findElements(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"));
		        for (WebElement ele2 : openRITM) {
		            ele2.click();
		           
		        }
		        String screenshotPath3 = ExtentReportManager.captureScreenshot(driver, "RITM");
		        test.info("Navigating to RITM", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
		        System.out.println(driver.getTitle());
		        Thread.sleep(3000);
		        
		        test.info("RITM: "+ritm);
		        
		        test.pass("Request and RITM opened Successfully");
		        
		        //Custom report
		        Reporter.getCurrentTestResult().setAttribute("TestData", requestno+" "+ritm);
		        
		        return ritm;
	}
	
	public String checkApprovals(ExtentTest test) throws InterruptedException {
		//Scroll down to related list
		test = ExtentReportManager.createTest("Verification of Generated Approvals");
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        
        driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[2]/span/span[2]")).click();
        
        List<WebElement> fetchapprovers = driver.findElements(By.xpath("//table[@id='sc_req_item.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println(fetchapprovers.size()+" Approver is generated");
        
        //Selecting approver
        if (!fetchapprovers.isEmpty()) {
            approver = fetchapprovers.get(0).getText();
        }
        System.out.println("Approver is : " +approver);
        test.info("Approver is : " +approver);
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "RITM");
        test.pass("Generated approvals", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
        //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", approver);
        
        return approver;
	}

}
