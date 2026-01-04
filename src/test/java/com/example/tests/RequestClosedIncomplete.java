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

import base.ApprovalHandling;
import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import utils.DataImport;
import utils.ExtentReportManager;


public class RequestClosedIncomplete extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	private JavascriptExecutor jse;
		
	//Object of Utils
	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);
	private ApprovalHandling approvalHandling = new ApprovalHandling(driver);
	
	//Variable Declaration
	private String requestno, ritm, catlogTask ;
	private String approver = null;
	private int TotalTask1, TotalTask2;

	//fetching excel data
	Object[][] requestData = DataImport.getData("Request");
		
	@Test(description = "Verification of Submitting the Big Data Analysis catalog item")
	public void createRequest() throws InterruptedException {
		test = ExtentReportManager.createTest("Verification of Submitting the Big Data Analysis catalog item").assignCategory("Request Closed Incomplete");
			
		driver.get(BaseTest.baseUrl + "/esc");
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
	   	test.info("Navigating the Employee Center", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			
		Thread.sleep(3000);
		String CatalogName = "Big Data Analysis";
		driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/input")).sendKeys(CatalogName);
		driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/span/button")).click();
		Thread.sleep(5000);
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
	   	test.info("Opening catalog form", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			
		test.info("Opening catalog form");
		driver.findElement(By.xpath("//mark[normalize-space()='Big Data Analysis']")).click();
		Thread.sleep(3000);
		System.out.println(CatalogName + " Form is visible");
		jse = (JavascriptExecutor) driver;
		//jse.executeScript("window.scrollBy(0,1500)");
		Thread.sleep(5000);
		WebElement fields = driver.findElement(By.xpath("// *[contains(text(),'Indicates required')]"));
		jse.executeScript("arguments[0].scrollIntoView();", fields);
		Thread.sleep(2000);
			
		test.info("Enter Dynamic Data");
		System.out.println("Select User");
		driver.findElement(By.xpath("//div[@id='s2id_sp_formfield_primary_contact']//b")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		//String primarycontact = (String) requestData[0][0];
		driver.findElement(By.xpath("//*[@id=\"s2id_autogen2_search\"]")).sendKeys((String) requestData[0][0]);
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//String primarycontact = "//div[normalize-space()='"+(String) requestData[0][0]+"']";
		driver.findElement(By.xpath("//div[normalize-space()='"+(String) requestData[0][0]+"']")).click();
		Thread.sleep(2000);
		System.out.println("Select Cost Center");
		driver.findElement(By.xpath("//*[@id=\"s2id_sp_formfield_cost_center\"]/a")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.findElement(By.xpath("//input[@id='s2id_autogen3_search']")).sendKeys((String) requestData[0][1]);
			
			//Select selectValu=new Select(element);
			//selectValu.selectByVisibleText("Development");
		driver.findElement(By.xpath("//div[@id=\"select2-drop\"]/ul/li/div[contains(text(), 'Development')]")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		Thread.sleep(2000);
		System.out.println("Select Business Purpose");
		String businessPurpose = "//span[normalize-space()='" + (String) requestData[0][2] + "']";
		driver.findElement(By.xpath(businessPurpose)).click();
		
		Thread.sleep(2000);
		System.out.println("Enter description");
		driver.findElement(By.xpath("//*[@id=\"sp_formfield_needs\"]")).sendKeys((String) requestData[0][3]);
		Thread.sleep(2000);
			
		System.out.println("Click on Submit");
		driver.findElement(By.xpath("//*[@id=\"submit-btn\"]")).click();
		System.out.println("Select Checkout");
		driver.findElement(By.xpath("//*[@id=\"sc_cat_checkout\"]/div[3]/div/div/button[2]/span[1]")).click();
		Thread.sleep(3000);
			
		requestno = driver.findElement(By.xpath("//*[@id=\"x6fb0f4029f8332002528d4b4232e70f6\"]/div[2]/div[2]/div/div[2]/b")).getText();
		System.out.println("Request No : " + requestno);
		Thread.sleep(3000);
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
	   	test.pass(requestno + " Request Created Successfully", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());        
	}
		
	@Test(description = "Verification of Request and RITM",dependsOnMethods = "createRequest")
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
        test.info("Approver is : " +approver);
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
        test.pass("Generated approvals", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
      //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", approver);
	}
	
	
	@Test(description = "Verification of Approving the approval by Impersonating user", dependsOnMethods = "approvals")
    public void impersonateUser() throws InterruptedException {
		Thread.sleep(3000);
        jse = (JavascriptExecutor) driver;
        
        //End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
        test = ExtentReportManager.createTest("Verification of Approving the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(approver, jse);
        
    	
    	// Approving the approval 
        approvalHandling.approveApproval(ritm, approver, test);
 
        //End impersonation
        test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
        Thread.sleep(3000);   
        
      //Impersonating adimn user
    	Object[][] users = DataImport.getData("ImpersonateUser");
    	impersonation.startImpersonation(users[0][0].toString(), jse);
    	Thread.sleep(2000);
	}
	
	@Test(description = "Verification of Catalog task after Approving the approval" , dependsOnMethods = "impersonateUser")
    public void verifyCatalogTask() throws InterruptedException {
		// Open Request & RITM after approval
		test = ExtentReportManager.createTest("Verification of Catalog task after Approving the approval");
		
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
        
        // Navigate to Catalog Task tab
   		System.out.println("navigate to SCTask tab");
		driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[1]/span/span[2]")).click();
					        

		// Open the Catalog item from related list  
		List<WebElement> catTask = driver.findElements(By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]"));
        Thread.sleep(2000);
        System.out.println("Total Tasks generated : " +catTask.size());
        TotalTask1 = catTask.size();
        for (WebElement cat1 : catTask) {
        	//String ApproverUser = Users.getText();
        	catlogTask = cat1.getText();
        	System.out.println(cat1.getText());
        	cat1.click();
        }
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
        test.info("Task Details", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
        test.pass("Catalog Task is generated");
        
      //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", catlogTask);
	}
	
	@Test(description = "Verification of Close Incompleating the Catalog task" , dependsOnMethods = "verifyCatalogTask")
    public void verifyCloseIncompleteCatalogTask() throws InterruptedException {
		// Closed Incomplete SCTask
		test = ExtentReportManager.createTest("Verification of Close Incompleating the Catalog task");
		WebElement taskState = driver.findElement(By.id("sc_task.state"));
		Select changeState = new Select(taskState);
		changeState.selectByVisibleText("Closed Incomplete");
		System.out.println("Task State changed to Closed Incomplete");
		driver.findElement(By.id("sysverb_update")).click();
						
		Thread.sleep(2000);
		System.out.println("RITM opened :" + driver.getTitle());
		driver.navigate().refresh();
				
				
		List<WebElement> catTask2 = driver.findElements(By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]"));
		Thread.sleep(2000);
		System.out.println("Total Tasks generated : " +catTask2.size());
		TotalTask2 = catTask2.size();

		Assert.assertEquals(TotalTask2, TotalTask1, "No more than 1 tasks are generated.");

		//Verify RITM State 
		WebElement ritmState = driver.findElement(By.id("sc_req_item.state"));
		Select selectRitmState = new Select(ritmState);
		WebElement selectedRitmState = selectRitmState.getFirstSelectedOption();
		
		test.info("RITM State is "+selectedRitmState.getText());
		System.out.println("RITM State is : "+selectedRitmState.getText());
		
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("RITM State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
		Assert.assertEquals(selectedRitmState.getText().trim(), "Closed Incomplete", "Test failed for " + ritm);

		//Verify Request State 		        
		for (int i = 0; i < 3; i++) {
		    driver.navigate().back();
		    driver.navigate().refresh();
		    Thread.sleep(2000);
		}
		        
		WebElement requestState = driver.findElement(By.id("sc_request.state"));
		Select selectReqState = new Select(requestState);
		WebElement selectedReqState = selectReqState.getFirstSelectedOption();
		
		test.info("Request State is "+selectedReqState.getText());
		System.out.println("REQUEST State is : "+selectedReqState.getText());
		
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Request State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
		Assert.assertEquals(selectedReqState.getText().trim(), "Closed Incomplete", "Test failed for " + ritm);
		
		test.pass("Request, RITM States are Closed Incomplete");
	}
}
