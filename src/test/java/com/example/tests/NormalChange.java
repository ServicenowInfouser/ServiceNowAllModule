
package com.example.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import utils.ExtentReportManager;


public class NormalChange extends BaseTest {
	private WebDriver driver = DriverManager.getDriver(); 
	String changeNo;
	private JavascriptExecutor jse;
    
    @Test(description = "Verification of Navigate to Change list")
    public void navigateToChangeList() throws InterruptedException {
    	//driver.get(baseUrl + "/change_request_list");
    	
    	jse = (JavascriptExecutor) driver;    
    	//Navigation through all menu
    	Navigator.allNavigation("change_request.list", driver, jse);
    }
    
    @Test(description = "Verification of Normal Change widget", dependsOnMethods = "navigateToChangeList")
    public void clickNormalChangeWidget() throws InterruptedException {
    	jse = (JavascriptExecutor) driver;
    	
    	//Clicking on New UI action
    	Navigator.newUIAction(driver, jse);
        
        Thread.sleep(2000);
        //Click on Models tab
        WebElement models=driver.findElement(By.xpath("//*[@id=\"change_models\"]"));
        models.click();
        
        Thread.sleep(5000);
        //Click on Normal widget
        WebElement NormalChange=driver.findElement(By.xpath("//*[@id=\"007c4001c343101035ae3f52c1d3aeb2\"]/div[1]/div[1]/span"));
        NormalChange.click();
    }
    
    @Test(description = "Verification of Creation of change", dependsOnMethods = "clickNormalChangeWidget")
    public void createChange() throws InterruptedException {
    	// Copy Change record number
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='change_request.number']"));
        changeNo = inputElement.getAttribute("value");
        System.out.println(changeNo);
    
        // Set Group to Assignment group field
        WebElement Assignmentgroup = driver.findElement(By.xpath("//*[@id=\"sys_display.change_request.assignment_group\"]"));
        Assignmentgroup.sendKeys("Application Development" + Keys.ENTER);
        
        Thread.sleep(2000);
        // Set user to Assign to field
        WebElement Assignto = driver.findElement(By.xpath("//*[@id=\"sys_display.change_request.assigned_to\"]"));
        Assignto.sendKeys("Arya Hajarha" + Keys.ENTER);

        Thread.sleep(2000);
        // Enter short description
        driver.findElement(By.xpath("//*[@id=\"change_request.short_description\"]")).sendKeys("Create NormalChannge");

        // Click on submit
        driver.findElement(By.xpath("//*[@id=\"sysverb_insert\"]")).click();
        
        Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
    }
    
    @Test(description = "Verification of opening Created change record from list", dependsOnMethods = "createChange")
    public void openChange() throws InterruptedException {
    	 driver.get(Config.baseUrl() + "/change_request_list");
         Thread.sleep(2000);
         // Search Change record on table
         WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
         globalSearchBox.sendKeys(changeNo);
         Thread.sleep(1000);
         globalSearchBox.sendKeys(Keys.ENTER);
         Thread.sleep(2000);

         // Open incident
         List<WebElement> openCHN = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]"));
         for (WebElement ele2 : openCHN) {
             String currentINC = ele2.getText();
             if (currentINC.contains(changeNo)) 
             {
                 ele2.click();
                 break;
             }
         }
         Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
    }
    
    @Test(description = "Verification of Generated Approvals", dependsOnMethods = "openChange")
    public void uiActionRequestApproval() throws InterruptedException {
    	//Click on RequestApproval UI action 
        WebElement RequestApproval=driver.findElement(By.xpath("//*[@id=\"state_model_request_assess_approval\"]"));
        RequestApproval.click();
        
        //String newbutton1="return document.querySelector(\"#state_model_request_assess_approval\")";
		//WebElement clicknnewui1 =(WebElement) jse.executeScript(newbutton1);
		//clicknnewui1.click();
    }
    
    @Test(description = "Verification of Approvals", dependsOnMethods = "uiActionRequestApproval")
    public void approvals() throws InterruptedException {
    	//Click on the Approvers tab
    	WebElement ApproversTab=driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[3]/span/span[2]"));
        ApproversTab.click();
        
        //Approval user
        List<WebElement> Approvers=driver.findElements(By.xpath("//*[@id=\"change_request.sysapproval_approver.sysapproval_table\"]/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println(Approvers.size());
        for (WebElement Users : Approvers) {
        	//String ApproverUser = Users.getText();
        	System.out.println(Users.getText());
        	Reporter.getCurrentTestResult().setAttribute("TestData", Users.getText());
       }
    }
    
    
    //@Test(description = "Verification of User Impersonation")
    public void impersonateUser() throws InterruptedException {
    	
    	jse = (JavascriptExecutor) driver;
    	
    	test = ExtentReportManager.createTest("Verification of Impersonation and End Impersonation");
    	test.info("Impersonation");
    	Impersonation.startImpersonation("Manifah Masood", driver, jse);
    	Thread.sleep(10000);
    	test.info("End Impersonation");
    	Impersonation.endImpersonation(driver, jse);
    	test.pass("success");
    	
    	//Impersonate to Approval user          
//        driver.get(Config.baseUrl());               
//        String profileIcon="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span > img\")";
//        WebElement profile=(WebElement)jse.executeScript(profileIcon);
//        profile.click();    
//        
//        String imp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div\")";
//        WebElement profileImpersonation=(WebElement)jse.executeScript(imp);
//        profileImpersonation.click();
//        
//        Thread.sleep(2000);
//        String searchUser="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\")";
//        WebElement serchprofile=(WebElement)jse.executeScript(searchUser);
//        serchprofile.sendKeys("Manifah Masood");
//        
//        Thread.sleep(2000);
//        String clickUser="return document.querySelector(\"body > now-popover-panel > seismic-hoist\").shadowRoot.querySelector(\"#\\\\31 832fbe1d701120035ae23c7ce610369\")";
//        WebElement clickUser1=(WebElement)jse.executeScript(clickUser);
//        clickUser1.click();
//        
//        Thread.sleep(2000);
//        String clickIMPbutton="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button > slot > span\")";
//        WebElement ipmButton=(WebElement)jse.executeScript(clickIMPbutton);
//        ipmButton.click();
//        
//        Thread.sleep(2000);
    }
    
    
    
    
     
}