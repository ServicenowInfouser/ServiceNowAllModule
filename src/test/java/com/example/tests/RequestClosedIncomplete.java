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

public class RequestClosedIncomplete extends BaseTest {
	
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
		
	@Test(description = "SC_007- Verification of Submitting the Big Data Analysis catalog item")
	public void createRequest() throws InterruptedException {
		test = ExtentReportManager.createTest("SC_007- Verification of Submitting the Big Data Analysis catalog item");
			
		requestno = requestritmtask.createRequest(test);
	}
	
	@Test(description = "SC_008- Verification of Request and RITM",dependsOnMethods = "createRequest")
    public void checkingRequest() throws InterruptedException {
        
		test = ExtentReportManager.createTest("SC_008- Verification of Submitted Request and RITM");

		ritm = requestritmtask.checkReqRITM(test, requestno);
    }
		
	@Test(description = "SC_009- Verification of Request and RITM", dependsOnMethods = "checkingRequest")
	public void approvals() throws InterruptedException {
		//Scroll down to related list
		test = ExtentReportManager.createTest("SC_009- Verification of Generated Approvals");

		approver = requestritmtask.checkApprovals(test);
	}
	
	@Test(description = "SC_010- Verification of Approving the approval by Impersonating user", dependsOnMethods = "approvals")
    public void impersonateUser() throws InterruptedException {
		Thread.sleep(3000);
        jse = (JavascriptExecutor) driver;
        
        //End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
        test = ExtentReportManager.createTest("SC_010- Verification of Approving the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(approver, jse);
        
    	Thread.sleep(2000); 
    	// Approving the approval 
        approvalHandling.approveApproval(ritm, approver, test);
        Thread.sleep(2000); 
        
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
	
	@Test(description = "SC_011- Verification of Catalog task after Approving the approval" , dependsOnMethods = "impersonateUser")
    public void verifyCatalogTask() throws InterruptedException {
		// Open Request & RITM after approval
		test = ExtentReportManager.createTest("SC_011- Verification of Catalog task after Approving the approval");
		
		catlogTask = requestritmtask.checkCatalogTask(test, requestno);
	}
	
	@Test(description = "SC_012- Verification of Close Incompleating the Catalog task" , dependsOnMethods = "verifyCatalogTask")
    public void verifyCloseIncompleteCatalogTask() throws InterruptedException {
		// Closed Incomplete SCTask
		test = ExtentReportManager.createTest("SC_012- Verification of Close Incompleating the Catalog task");

		requestritmtask.closeIncompleteCatalogTask(test, catlogTask);
	}
}
