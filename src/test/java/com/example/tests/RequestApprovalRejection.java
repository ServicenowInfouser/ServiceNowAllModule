package com.example.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.ApprovalHandling;
import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import utils.DataImport;
import utils.ExtentReportManager;


public class RequestApprovalRejection extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
		
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
	
	SoftAssert softAssert = new SoftAssert();
	
	@Test(description = "SC_013- Verification of Submitting the Big Data Analysis catalog item")
	public void createRequest() throws InterruptedException {
		test = ExtentReportManager.createTest("SC_013- Verification of Submitting the Big Data Analysis catalog item").assignCategory("Request Approval Rejection");
		
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
    	
    	//Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
    }
	
	@Test(description = "SC_014- Verification of Request and RITM",dependsOnMethods = "createRequest")
    public void checkingRequest() throws InterruptedException {
        
		test = ExtentReportManager.createTest("SC_014- Verification of Submitted Request and RITM");

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
        String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
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
        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
    	test.info("Request details", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
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
        String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
        test.info("RITM details", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
        System.out.println(driver.getTitle());
        Thread.sleep(3000);
        
        test.info("RITM: "+ritm);
        
        test.pass("Request and RITM opened Successfully");
        
      //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", requestno+" "+ritm);
        
    }
	
	@Test(description = "SC_015- Verification of Generated Approvals", dependsOnMethods = "checkingRequest")
    public void approvals() throws InterruptedException {
		//Scroll down to related list
		test = ExtentReportManager.createTest("SC_015- Verification of Generated Approvals");
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
    	approvalHandling.RejectApproval(ritm, approver, test);
        
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