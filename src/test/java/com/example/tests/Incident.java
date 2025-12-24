
package com.example.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.DriverManager;
import base.Navigator;
import pages.LoginPage;

public class Incident {
	private WebDriver driver; 
	private LoginPage loginPage;
	String incNo;
	String baseUrl = "https://dev311431.service-now.com";
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
	
	@BeforeClass 
	public void setUpBrowser() throws InterruptedException { 
		driver = DriverManager.getDriver(); 
		loginPage = new LoginPage(driver); 
		
		loginPage.login("admin", "w^M8e%GurWP0");
	} 
	
	@AfterClass 
	public void tearDownBrowser() { 
		DriverManager.quitDriver(); 
	}

    @AfterMethod(alwaysRun = true)
    public void onFailure(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                Path out = Path.of("target", "screenshots", result.getName() + "_" + stamp + ".png");
                Files.createDirectories(out.getParent());
                Files.write(out, png);
                System.out.println("Saved screenshot: " + out.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }
    
    @Test(description = "Verification of Navigate to Incident list")
    public void navigateToIncidentList() throws InterruptedException {
    	//driver.get(baseUrl + "/change_request_list");
    	
    	jse = (JavascriptExecutor) driver;    	
    	
    	//Navigation through all menu and opening list
    	Navigator.allNavigation("incident.list", driver, jse);
    }
    
    @Test(description = "Verification of Create Incident", dependsOnMethods = "navigateToIncidentList")
    public void createIncident() throws InterruptedException {
        
    	jse = (JavascriptExecutor) driver;
    	
    	//Click on the New UI action
    	Navigator.newUIAction(driver, jse);
    
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