package com.example.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import junit.framework.Assert;
import utils.DataImport;
import utils.ExtentReportManager;

public class NormalChangeRejection extends BaseTest
{
	private WebDriver driver = DriverManager.getDriver(); 
	String changeNo;
	private JavascriptExecutor jse;
	String firstAprovalUser=null; 
		
	Object[][] changedata = DataImport.getData("Normal_Change");
	
	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);;
	
	/*
	 * @expected:User provided value
	 * @actual: UI value
	 */
	public static void compareTwoStringsEquals(String expected,String actual) {
		Assert.assertEquals(expected, actual);
	}
    
    @Test(description = "Verification of Navigate to Change list")
    public void navigateToChangeList() throws InterruptedException {
    	jse = (JavascriptExecutor) driver;
    	test = ExtentReportManager.createTest("Verification of Navigate to Incident list");
    	
    	jse = (JavascriptExecutor) driver;    
    	//Navigation through all menu
    	test.info("Open Change list from All menu");
    	//navigator = new Navigator(driver);
    	navigator.allNavigation("change_request.list", jse);

    	test.info("Clicking on the New UI action");
    	//Click on the New UI action
    	navigator.newUIAction(jse);
        
        Thread.sleep(4000);
        //Click on Models tab
        WebElement models=driver.findElement(By.xpath("//*[@id='change_models']"));
        models.click();
        
        test.info("Clicking on the Normal change widget");
        Thread.sleep(5000);
        
        //Click on Normal widget
        WebElement NormalChange=driver.findElement(By.xpath("//*[@id='007c4001c343101035ae3f52c1d3aeb2']/div[1]/div[1]/span"));
        NormalChange.click();
        test.pass("Navigated to the New change page");
    }
    
    @Test(description = "Verification of Creation of change", dependsOnMethods = "navigateToChangeList")
    public void createChange() throws InterruptedException 
    {
    	// Create a object of getData method
    	System.out.println(changedata);
    	String short_description = changedata[0][0].toString();
    	
    	test = ExtentReportManager.createTest("Verification of Navigating to Change list view");
    	
    	Thread.sleep(2000);
    	String state1=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='New']")).getText();
    	System.out.println("State is:"+state1);
    	compareTwoStringsEquals("New", state1);
    	String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
    	test.info("Verify the State of record", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    	
    	// Copy Change record number
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='change_request.number']"));
        changeNo = inputElement.getAttribute("value");
        System.out.println("Change Number is:"+changeNo);
        
        Thread.sleep(2000);
        // Enter short description
        driver.findElement(By.xpath("//*[@id='change_request.short_description']")).sendKeys(short_description);

        test.info("verification of Submit UI Action");
        // Click on submit
        driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
        
        test.pass("Change record created : "+changeNo);
        Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
        }
    
    @Test(description = "Verification of opening Created change record from list", dependsOnMethods = "createChange")
    public void openChange() throws InterruptedException 
    {
    	test = ExtentReportManager.createTest("Opening Change record after Submition"); 
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
        
        String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
        test.pass("Change record opened from the list view", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
    }
    
    @Test(description = "Verification of Approval Generation", dependsOnMethods = "openChange")
    public void requestingApproval() throws InterruptedException 
    {
    	String Assignmentgroup = changedata[0][1].toString();
    	String Assignto = changedata[0][2].toString();
    	
    	Thread.sleep(10000);
    	
    	test = ExtentReportManager.createTest("Verification of Request Approval UI action");
    	
    	//Click on RequestApproval UI action without fill Assignment group and Assign to fields
    	test.info("Click on the Request Approval UI action without fill Assignment group and Assign to fields");
        WebElement RequestApproval=driver.findElement(By.xpath("//*[@id='state_model_request_assess_approval']"));
        RequestApproval.click();
        
       //Verification of Error massage
        test.info("Verification of Error massage");
        Thread.sleep(2000);
        String errorMassage1=driver.findElement(By.xpath("//*[@id=\"output_messages\"]/div/div/span[2]")).getText();
        System.out.println("Error massage is :"+errorMassage1);

        // Set Group to Assignment group field
        test.info("Verification on fields on change record");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='sys_display.change_request.assignment_group']")).sendKeys(Assignmentgroup);
        
       // Set user to Assign to field
        test.info("Set user to Assign to field");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='sys_display.change_request.assigned_to']")).sendKeys(Assignto);
         
      //Click on RequestApproval UI action after fill Assignment group and Assign to fields
    	test.info("Click on the Request Approval UI action after fill Assignment group and Assign to fields");
    	Thread.sleep(2000);
        WebElement RequestApproval2=driver.findElement(By.xpath("//*[@id='state_model_request_assess_approval']"));
        RequestApproval2.click();

        //Click on the Approvers tab
        test.info("Click on the Approvers tab");
        Thread.sleep(2000);
    	WebElement ApproversTab=driver.findElement(By.xpath("//*[@id='tabs2_list']/span[3]/span/span[2]"));
        ApproversTab.click();
        
        //Verification of State after Click on the Approvers tab
        test.info("Verification of State after Click on the Approvers tab");
        String state2=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Assess']")).getText();
    	System.out.println("State is:"+state2);
    	compareTwoStringsEquals("Assess", state2);
        
        //Approval user
        List<WebElement> Approvers=driver.findElements(By.xpath("//*[@id='change_request.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]"));
        Thread.sleep(2000);
        
    
        if(Approvers.isEmpty()) {
        	System.out.println("No user found on the page.");
        }
        System.out.println("Total user found: "+Approvers.size());	
        	
        for(int i=0;i<Approvers.size();i++) {
        	String userName=Approvers.get(i).getText().trim();
        	test.info("Approver user " + Approvers.get(i).getText().trim());
        	System.out.println("User "+ (i+1)+ ":"+userName);
        	//store first userName
        	if(i==0) {
        		firstAprovalUser=userName;
        	}
        }
        System.out.println("First User Name is: "+firstAprovalUser);
       test.pass("Approvals generated successfully");
    }
    
    
    @Test(description = "Verification of User Impersonation", dependsOnMethods = "requestingApproval")
    public void impersonateUser() throws InterruptedException 
    {
    	String comment = changedata[0][3].toString();
    	
    	Thread.sleep(5000);
    	jse = (JavascriptExecutor) driver;
    	
    	//End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
    	test = ExtentReportManager.createTest("Verification of Rejecting the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(firstAprovalUser, jse);
    	Thread.sleep(2000);
        driver.get(baseUrl + "/sysapproval_approver_list");
        Thread.sleep(2000);
        
        //Search Change record in Approval table
        test.info(" Search Change record in Approval table");
        WebElement Approvals=driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
        Approvals.click();
        Thread.sleep(3000);
        Select selectValu=new Select(Approvals);
        selectValu.selectByVisibleText("Approval for");
        
        //Search change number in search bar 
        test.info("Search change number in search bar");
        Thread.sleep(2000);
        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox2.sendKeys(changeNo);
        Thread.sleep(2000);
        globalSearchBox2.sendKeys(Keys.ENTER);
        
        //Search Approver name in search bar
        test.info("Search Approver name in search bar");
        WebElement approversearch=driver.findElement(By.xpath("//*[@id='sysapproval_approver_table']/thead/tr[2]/td[4]/div/div/div/input"));
        approversearch.sendKeys(firstAprovalUser);
        approversearch.sendKeys(Keys.ENTER);
       
        //Opening Approval Change record
        test.info("Opening Approval Change record");
        WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
        requestedbutton.click();
    
      //Click on Reject UI Action without adding comment
        test.info("Click on Reject UI Action without adding comment");
        driver.findElement(By.xpath("//*[@id='reject']")).click();
        Thread.sleep(2000);
        
        //Verification of Error massage
        test.info("Verification of Error massage");
        String error=driver.findElement(By.xpath("//div[contains(text(),'Comments are required when rejecting an approval')]")).getText();
        System.out.println("Error massage is :"+error);
        
        //Adding comment in comment secssion
        test.info("Adding comment in comment secssion");
        driver.findElement(By.xpath("//*[@class='sn-string-textarea form-control ng-pristine ng-untouched ng-valid ng-isolate-scope ng-empty']")).sendKeys(comment);
        
        //Click on Post button
        test.info("Click on Post button");
        WebElement post=driver.findElement(By.xpath("//button[@class='btn btn-default activity-submit']"));
        post.isDisplayed();
        post.click();
        
        //click on Reject UI Action
        test.info("click on Reject UI Action");
        driver.findElement(By.xpath("//*[@id='reject']")).click();
        Thread.sleep(2000);
    	
        //End Impersonation
    	test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully Reject the approval");
    	
    	//Impersonating adimn user
    	Object[][] users = DataImport.getData("ImpersonateUser");
    	impersonation.startImpersonation(users[0][0].toString(), jse);
    	Thread.sleep(2000);
    }
    
    @Test(description = "Verification of Change record after Rejection of 1st Approval", dependsOnMethods = "impersonateUser")
    public void OPNCHNAFTREJ() throws InterruptedException {
    	test = ExtentReportManager.createTest("Verification of Change record after Rejection of 1st Approval");
    	
        //Opening Change record After 1st Approval
    	driver.get(Config.baseUrl() + "/change_request_list");
    	Thread.sleep(2000);
    	
    	// Search Change record on table
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(changeNo);
        Thread.sleep(1000);
        globalSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        List<WebElement> openChange = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]/a"));
        for (WebElement chan1 : openChange) {
            String currentChan = chan1.getText();
            if (currentChan.contains(changeNo)) 
            {
          	  chan1.click();
                break;
            }
        }
    	
        //Verification of State after 1st Approval Rejected
        System.out.println("Opening the Change record after 1St Approval Rejected");
        test.info("Verification of State after 1st Approval Rejected");
    	
        String state3=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='New']")).getText();
    	System.out.println("State is:"+state3);
        compareTwoStringsEquals("New", state3);
        test.pass("Rejection flow is completed");
        Thread.sleep(5000);
    }
}
