package com.example.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import utils.ExtentReportManager;
import utils.Log;


public class RequestApprovalRejection extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	private JavascriptExecutor jse;
		
	//Object of Utils
	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);
	
	//Variable Declaration
	private String requestno = "REQ0010022";
	private String ritm;
	private String approver = null;
	private int TotalTask1, TotalTask2;
	
	@Test(description = "Verification of Request and RITM")
    public void createRequest() throws InterruptedException {
		Log.info("Starting login test...");
		test = ExtentReportManager.createTest("Verification of Submitting Request Standing desk form");

    	driver.get(BaseTest.baseUrl + "/esc");
    	driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    	String screenshotPath = ExtentReportManager.captureScreenshot(driver, "esc");
    	test.info("Navigating to ESC Portal", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        System.out.println(driver.getTitle());
	}

	
	@Test(description = "Verification of Request and RITM")
    public void checkingRequest() throws InterruptedException {
        
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
    	test.info("Open Generated Request", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
        
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
        
    }
	
	@Test(description = "Verification of Generated Approvals", dependsOnMethods = "checkingRequest")
    public void approvals() throws InterruptedException {
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
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "RITM");
        test.info("Approvers generated", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}
	
	
	@Test(description = "Verification of Rejecting the approval by Impersonating user", dependsOnMethods = "approvals")
    public void impersonateUser() throws InterruptedException {
		Thread.sleep(3000);
        jse = (JavascriptExecutor) driver;
        test = ExtentReportManager.createTest("Verification of Rejecting the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(approver, jse);
        
    	
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
        System.out.println("Request approval for :"+ritm);
        globalSearchBox23.sendKeys(ritm);
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
        //End impersonation
        test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
        Thread.sleep(3000);  
	}
	
	
	@Test(description = "Verification of SCTask, Request & RITM after Rejecting the approval" , dependsOnMethods = "impersonateUser")
    public void verifyCatalogTask() throws InterruptedException {
		// Open Request & RITM after approval
		test = ExtentReportManager.createTest("Verification of SCTask, Request & RITM after Rejecting the approval");
		driver.get(BaseTest.baseUrl + "/sc_request_list");
        System.out.println(requestno);
        Thread.sleep(2000);
        
     // Search Request
        driver.findElement(By.xpath("//input[@class='form-control' and @type='search']")).sendKeys(requestno + Keys.ENTER);
        
        List<WebElement> openReq1 = driver.findElements(By.xpath("//table[@id='sc_request_table']/tbody/tr/td[3]/a"));
        for (WebElement ele2 : openReq1) {
            String currentReq = ele2.getText();
            if (currentReq.contains(requestno)) 
            {
                ele2.click();
                break;
            }
        }
        
        List<WebElement> openRitmAfterApproval = driver.findElements(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"));
        for (WebElement ele2 : openRitmAfterApproval) {
            ele2.click();
        }
        
		//SCTask verification		
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        
		System.out.println("navigate to SCTask tab");
      	driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[1]/span/span[2]")).click();
      
      	// Open the Catalog item from related list  
      	
      	List<WebElement> catTask = driver.findElements(By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]"));
        Thread.sleep(2000);
        
        Assert.assertTrue(catTask.size() > 0,"SCTask is not generated");
        
        String screenshotPath1 = ExtentReportManager.captureScreenshot(driver, "Approval");
        test.info("Task is not generated", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
        
        WebElement ritmState = driver.findElement(By.id("sc_req_item.state"));
        Select selectRitmState = new Select(ritmState);
        WebElement selectedRitmState = selectRitmState.getFirstSelectedOption();
        System.out.println("RITM State is : "+selectedRitmState.getText());
        String screenshotPath2 = ExtentReportManager.captureScreenshot(driver, "ritmstate");
        test.info("RITM State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        Assert.assertEquals(selectedRitmState.getText().trim(), "Closed Incomplete",  "Test failed for " + ritm );
       
        Thread.sleep(3000);
        driver.navigate().back();
        //System.out.println("opening request for state validation");
        Thread.sleep(3000);
        
        WebElement requestState = driver.findElement(By.id("sc_request.state"));
        Select selectReqState = new Select(requestState);
        WebElement selectedReqState = selectReqState.getFirstSelectedOption();
        System.out.println("REQUEST State is : "+selectedReqState.getText());
        String screenshotPath3 = ExtentReportManager.captureScreenshot(driver, "reqstate");
        test.info("Request State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
        Assert.assertEquals(selectedReqState.getText().trim(),"Closed Incomplete", "Test failed for " + ritm);
        
	}
	
}
