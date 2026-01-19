package com.example.tests;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import base.ApprovalHandling;
import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import pages.RequestRITMTask;
import utils.DataImport;
import utils.ExtentReportManager;

public class RequestClosedComplete extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	private JavascriptExecutor jse;
		
	//Object of Utils
	private Impersonation impersonation = new Impersonation(driver);
	private ApprovalHandling approvalHandling = new ApprovalHandling(driver);
	private RequestRITMTask requestritmtask = new RequestRITMTask(driver);
	
	//Variable Declaration
	private String requestno, ritm, catlogTask ;
	private String approver = null;

	//fetching excel data
	Object[][] requestData = DataImport.getData("Request");
	
	
	@Test(description = "SC_001- Verification of Submitting the Big Data Analysis catalog item")
	public void createRequest() throws InterruptedException {
		test1 = ExtentReportManager.createTest("------- Request Flow Started -------");
		
		test = ExtentReportManager.createTest("SC_001- Verification of Submitting the Big Data Analysis catalog item");
		
		requestno = requestritmtask.createRequest(test);
    }
	
	@Test(description = "SC_002- Verification of Request and RITM",dependsOnMethods = "createRequest")
    public void checkingRequest() throws InterruptedException {
		
		test = ExtentReportManager.createTest("SC_002- Verification of Submitted Request and RITM");
		
		ritm = requestritmtask.checkReqRITM(test, requestno);
    }
	
	@Test(description = "SC_003- Verification of Generated Approvals", dependsOnMethods = "checkingRequest")
	public void approvals() throws InterruptedException {
		
		test = ExtentReportManager.createTest("SC_003- Verification of Generated Approvals");
		
		approver = requestritmtask.checkApprovals(test);
	}
	
	@Test(description = "SC_004- Verification of Approving the approval by Impersonating user", dependsOnMethods = "approvals")
    public void impersonateUser() throws InterruptedException {
		Thread.sleep(3000);
        jse = (JavascriptExecutor) driver;
        
        //End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
        test = ExtentReportManager.createTest("SC_004- Verification of Approving the approval by Impersonating user");
    	test.info("Impersonated the approver user : "+ approver);
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
	
	@Test(description = "SC_005- Verification of Catalog task after Approving the approval" , dependsOnMethods = "impersonateUser")
    public void verifyCatalogTask() throws InterruptedException {
		// Open Request & RITM after approval
		test = ExtentReportManager.createTest("SC_005- Verification of Catalog task after Approving the approval");
		
		catlogTask = requestritmtask.checkCatalogTask(test, requestno);
	}
	
	@Test(description = "SC_006- Verification of Close Compleating the Catalog task" , dependsOnMethods = "verifyCatalogTask")
    public void verifyCloseCompleteCatalogTask() throws InterruptedException {
		// Closed Complete SCTask
		test = ExtentReportManager.createTest("SC_006- Verification of Close Compleating the Catalog task");
		
		requestritmtask.closeCompleteCatalogTask(test, catlogTask);
	}
}
