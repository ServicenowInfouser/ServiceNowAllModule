package com.example.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.ApprovalHandling;
import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import pages.RequestRITMTask;
import utils.DataImport;
import utils.ExtentReportManager;


public class RequestApprovalRejection extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
		
	//Object of Utils
	private Impersonation impersonation = new Impersonation(driver);
	private ApprovalHandling approvalHandling = new ApprovalHandling(driver);
	private RequestRITMTask requestritmtask = new RequestRITMTask(driver);
	
	//Variable Declaration
	private String requestno, ritm;
	private String approver = null;

	//fetching excel data
	Object[][] requestData = DataImport.getData("Request");
	
	SoftAssert softAssert = new SoftAssert();
	
	@Test(description = "SC_013- Verification of Submitting the Big Data Analysis catalog item")
	public void createRequest() throws InterruptedException {
		test = ExtentReportManager.createTest("SC_013- Verification of Submitting the Big Data Analysis catalog item");
		
		requestno = requestritmtask.createRequest(test);
    }
	
	@Test(description = "SC_014- Verification of Request and RITM",dependsOnMethods = "createRequest")
    public void checkingRequest() throws InterruptedException {
        
		test = ExtentReportManager.createTest("SC_014- Verification of Submitted Request and RITM");

		ritm = requestritmtask.checkReqRITM(test, requestno);
    }
	
	@Test(description = "SC_015- Verification of Generated Approvals", dependsOnMethods = "checkingRequest")
    public void approvals() throws InterruptedException {
		//Scroll down to related list
		test = ExtentReportManager.createTest("SC_015- Verification of Generated Approvals");

		approver = requestritmtask.checkApprovals(test);
	}
	
	
	@Test(description = "SC_016- Verification of Rejecting the approval by Impersonating user", dependsOnMethods = "approvals")
    public void impersonateUser() throws InterruptedException {
		Thread.sleep(3000);
        jse = (JavascriptExecutor) driver;
        
        //End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
        
        test = ExtentReportManager.createTest("SC_016- Verification of Rejecting the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(approver, jse);
        
    	
    	// Rejecting the approval 
    	approvalHandling.rejectApproval(ritm, approver, test);
        
        //End impersonation
        test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully Rejected the approval by impersonating user");
        Thread.sleep(3000);  
        
      //Impersonating adimn user
    	Object[][] users = DataImport.getData("ImpersonateUser");
    	impersonation.startImpersonation(users[0][0].toString(), jse);
    	Thread.sleep(2000);
	}
	
	
	@Test(description = "SC_017- Verification of SCTask, Request & RITM after Rejecting the approval" , dependsOnMethods = "impersonateUser")
    public void verifyCatalogTask() throws InterruptedException {
		// Open Request & RITM after approval
		test = ExtentReportManager.createTest("SC_017- Verification of SCTask, Request & RITM after Rejecting the approval");
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
        
        softAssert.assertTrue(catTask.size() > 0,"SCTask is not generated");
        
        String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
        test.info("Task is not generated", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
        
        WebElement ritmState = driver.findElement(By.id("sc_req_item.state"));
        Select selectRitmState = new Select(ritmState);
        WebElement selectedRitmState = selectRitmState.getFirstSelectedOption();
        
        test.info("RITM State is "+selectedRitmState.getText());
        System.out.println("RITM State is : "+selectedRitmState.getText());
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
        test.info("RITM State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        softAssert.assertEquals(selectedRitmState.getText().trim(), "Closed Incomplete",  "Test failed for " + ritm );
       
        Thread.sleep(3000);
        driver.navigate().back();
        //System.out.println("opening request for state validation");
        Thread.sleep(3000);
        
        WebElement requestState = driver.findElement(By.id("sc_request.state"));
        Select selectReqState = new Select(requestState);
        WebElement selectedReqState = selectReqState.getFirstSelectedOption();
        
        test.info("RITM State is "+selectedReqState.getText());
        System.out.println("REQUEST State is : "+selectedReqState.getText());
        
        String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
        test.info("Request State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
        softAssert.assertEquals(selectedReqState.getText().trim(),"Closed Incomplete", "Test failed for " + ritm);
        
        test.pass("Request, RITM States are Closed Incomplete");
	}
}