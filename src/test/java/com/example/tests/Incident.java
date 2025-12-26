
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
    	
    	//Navigation through all menu and opening list
    	test.info("Open Incident list from All menu");
    	navigator = new Navigator(driver);
    	navigator.allNavigation("incident.list", jse);
    	
    	test.pass("Navigated to the Incident list");
    }
    
    @Test(description = "Verification of Create Incident", dependsOnMethods = "navigateToIncidentList")
    public void createIncident() throws InterruptedException {
        
    	JavascriptExecutor jse = (JavascriptExecutor) driver;
    	
    	test = ExtentReportManager.createTest("Verification of Creation of Incident");
    	test.info("Clicking on the New UI action");
    	//Click on the New UI action
    	navigator.newUIAction(jse);
    
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