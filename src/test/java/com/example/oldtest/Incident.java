
package com.example.oldtest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.Test;

import base.BaseTest;
import base.DriverManager;
import base.Navigator;
import utils.ExtentReportManager;

public class Incident extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver(); 
	String incNo;
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
	
	private Navigator navigator;
	
    
    @Test(description = "Verification of Navigate to Incident list")
    public void navigateToIncidentList() throws InterruptedException {  	
    	jse = (JavascriptExecutor) driver;
    	test = ExtentReportManager.createTest("Verification of Navigate to Incident list");
    	
    	Thread.sleep(3000);
    	//Navigation through all menu and opening list
    	System.out.println("test1");
    	String all="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";
        WebElement allclick=(WebElement)jse.executeScript(all);
        allclick.click();
		
		System.out.println("test2");
		//test.info("Search and open list");
		String filter="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\").shadowRoot.querySelector(\"#filter\")";
		WebElement filtertype=(WebElement)jse.executeScript(filter);
		filtertype.sendKeys("incident.list");
		filtertype.sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		System.out.println("test3");
		String iframe="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"#gsft_main\")";
		WebElement frame=(WebElement) jse.executeScript(iframe);
		driver.switchTo().frame(frame);
		Thread.sleep(3000);
    	
//    	test.info("Open Incident list from All menu");
//    	navigator = new Navigator(driver);
//    	navigator.allNavigation("incident.list", jse);
    	
    	test.pass("Navigated to the Incident list");
    }
    
    @Test(description = "Verification of Create Incident", dependsOnMethods = "navigateToIncidentList")
    public void createIncident() throws InterruptedException {
        
    	JavascriptExecutor jse = (JavascriptExecutor) driver;
    	
    	test = ExtentReportManager.createTest("Verification of Creation of Incident");
    	test.info("Clicking on the New UI action");
    	//Click on the New UI action
    	navigator.newUIAction();
    
        Thread.sleep(2000);
        // Copy Incident record number
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
        incNo = inputElement.getAttribute("value");
        System.out.println(incNo);
    
        // Set caller value
        WebElement caller = driver.findElement(By.xpath("//*[@id='sys_display.incident.caller_id']"));
        caller.sendKeys("Abel Tuter" + Keys.ENTER);

        Thread.sleep(2000);
        // Enter short description
        driver.findElement(By.xpath("//*[@id='incident.short_description']")).sendKeys("Create incident");

        // Click on submit
        driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
        
        // Attach custom test data to report
        Reporter.getCurrentTestResult().setAttribute("TestData", incNo);
        test.pass(incNo + " Incident record created");
    } 
    
	@Test(description = "Verification of Incident", dependsOnMethods = "createIncident")
    public void checkingRequest() throws InterruptedException {
        
        Thread.sleep(2000);
        // Search Incident record on table
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(incNo);
        Thread.sleep(1000);
        globalSearchBox.sendKeys(Keys.ENTER);

        Thread.sleep(2000);

        // Open incident
        List<WebElement> openINC = driver.findElements(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]"));
        for (WebElement ele2 : openINC) {
            String currentINC = ele2.getText();
            if (currentINC.contains(incNo)) {
                ele2.click();
                break;
            }
        }
        
        // Attach custom test data to report
        Reporter.getCurrentTestResult().setAttribute("TestData", incNo);
        System.out.println("Execution completed");   
    } 
}