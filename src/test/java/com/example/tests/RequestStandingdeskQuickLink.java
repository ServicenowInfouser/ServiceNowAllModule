
package com.example.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import utils.DataImport;
import utils.Log;
import utils.ExtentReportManager;

public class RequestStandingdeskQuickLink extends BaseTest{
	private WebDriver driver = DriverManager.getDriver(); 
	String requestno;
	Object[][] users = DataImport.getData("ImpersonateUser");
	
	
	
	@Test(description = "Verification of ESC Portal Quick links" )
    public void submitForm() throws InterruptedException {
        
    	Log.info("Starting login test...");
		test = ExtentReportManager.createTest("Verification of Submitting Request Standing desk form");

    	driver.get(BaseTest.baseUrl + "/esc");
    	driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    	String screenshotPath = ExtentReportManager.captureScreenshot(driver, "esc");
    	test.info("Navigating to ESC Portal", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        System.out.println(driver.getTitle());
        
        test.info("Clicking on the Request Standing desk Quick Link");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.xpath("//*[@id=\"a98711ea53331210b8f2ddeeff7b12ba-item-"+1+"\"]")).click();
        //*[@id="a98711ea53331210b8f2ddeeff7b12ba-item-1"]/div[2]/div/span
        //document.querySelector("#a98711ea53331210b8f2ddeeff7b12ba-item-1 > div.content-container > div > span")
        //#a98711ea53331210b8f2ddeeff7b12ba-item-1 > div.content-container > div > span
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        System.out.println(driver.getTitle());

		//test.info("Provide Input values");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.findElement(By.xpath("//*[@id=\"sp_formfield_ergonomic_office\"]")).sendKeys("test");
        String screenshotPath1 = ExtentReportManager.captureScreenshot(driver, "form");
    	test.info("Providing input values", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
        
        test.info("Click on the Order Now button");
        driver.findElement(By.xpath("//*[@id=\"submit-btn\"]")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        //Submitting Request Standing desk form
        test.info("Click on the Checkout button");
        driver.findElement(By.xpath("//*[@id=\"sc_cat_checkout\"]/div[3]/div/div/button[2]")).click();
        
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        requestno = driver.findElement(By.xpath("//*[@id=\"x6fb0f4029f8332002528d4b4232e70f6\"]/div[2]/div[2]/div/div[2]/b")).getText();
        Thread.sleep(2000);
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "req");
    	test.info("", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        System.out.println(requestno); 
        
        test.info("Request no: " + requestno);
        // Attach custom test data to report
        Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
        Thread.sleep(2000);
        
        test.pass("Request Submitted Successfully");
    }
	
	@Test(description = "Verification of Request", dependsOnMethods = "submitForm")
    public void checkingRequest() throws InterruptedException {
        
		test = ExtentReportManager.createTest("Verification of Submitted Request");

		//Navigating to Request List
		test.info("Navigating to Request List view");
        driver.get(BaseTest.baseUrl + "/sc_request_list");
        System.out.println(requestno);
        Thread.sleep(2000);
        
        // Search Request
        test.info("Searching for the created Request");
        //WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @typ='search']"));
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(requestno + Keys.ENTER);
        String screenshotPath1 = ExtentReportManager.captureScreenshot(driver, "list");
    	test.info("Navigating to ESC Portal", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
        
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
    	test.info("Navigating to ESC Portal", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
        // Attach custom test data to report
        //Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
        System.out.println(driver.getTitle());
        Thread.sleep(2000);
        
        test.pass("Request opened Successfully");
    }
	
	@Test(description = "Verification of RITM", dependsOnMethods = "checkingRequest")
    public void checkingRITM() throws InterruptedException {

        test = ExtentReportManager.createTest("Verification of Submitting RITM");

		//Open RITM
		test.info("Open RITM from the Requested Item Related List");
		String ritm = driver.findElement(By.xpath("d//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a")).getText();
        List<WebElement> openRITM = driver.findElements(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"));
        for (WebElement ele2 : openRITM) {
            ele2.click();
            // Attach custom test data to report
            Reporter.getCurrentTestResult().setAttribute("TestData", ritm);
        }
        System.out.println(driver.getTitle());
        Thread.sleep(3000);
        
        test.info("RITM: "+ritm);
        
        test.pass("RITM opened Successfully");
        
        //System.out.println(driver.findElement(By.xpath("//*[@id=\"sys_display.sc_req_item.cat_item\"]")).getText()); 
    }
}
