
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

import base.Navigator;

public class PasswordResetQuicklinks {
	
	private WebDriver driver; 
	String INCnumber;
	String baseUrl = "https://dev311431.service-now.com";
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
	
	private Navigator navigator;
    
    @Test(description = "Verification of Password Reset Quick links" )
    public void clickPasswordResetQuicklinks() {
        
    	driver.get(baseUrl + "/esc");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        System.out.println(driver.getTitle());
        Reporter.log("User Navigated to ESC portal");
        
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.xpath("//*[contains(text(), 'Password reset')]")).click();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        //System.out.println(driver.getTitle());
    }
    
    @Test(description = "Verification of Submitting Password Reset Quick links", dependsOnMethods = "clickPasswordResetQuicklinks")
    public void submittingPasswordResetForm() throws InterruptedException {
        
    	//Short description
        String shortDescription = driver.findElement(By.xpath("//*[@id='catItemTop']/div/div[1]/div[1]/div")).getText();
        System.out.println(shortDescription);

        //Que 1
        String Que1 = driver.findElement(By.xpath("//*[contains(text(), 'Whose password needs to be reset?')]")).getText();
        System.out.println(Que1);

        //Select choice in Que 1
        WebElement searchBox = driver.findElement(By.xpath("//div[@id='s2id_sp_formfield_caller_id']/a"));
        searchBox.click();
        Thread.sleep(2000);

        List<WebElement> options = driver.findElements(By.xpath("//ul[@class='select2-results']/li"));
        for (WebElement ele : options) {
            String currentOption = ele.getText();
            if (currentOption.contains("Chad Miklas")) {
                ele.click();
                break;
            }
        }

        //Que 2
        String Que2 = driver.findElement(By.xpath("//*[contains(text(), 'What application password do you need reset?')]")).getText();
        System.out.println(Que2);

        //Select choice in Que 2
        WebElement searchBox1 = driver.findElement(By.xpath("//*[@id='s2id_sp_formfield_cmdb_ci']/a"));
        searchBox1.click();
        Thread.sleep(2000);

        List<WebElement> options1 = driver.findElements(By.xpath("//ul[@class='select2-results']/li"));
        for (WebElement ele1 : options1) {
            String currentOption1 = ele1.getText();
            if (currentOption1.contains("Email")) {
                ele1.click();
                break;
            }
        }

        // Click on Submit button
        driver.findElement(By.xpath("//*[@id='submit-btn']")).click();

        INCnumber = driver.findElement(By.id("data.number.name")).getText();
        System.out.println(INCnumber);

    }
    
    @Test(description = "Verification of Created Incident", dependsOnMethods = "submittingPasswordResetForm")
    public void checkingIncident() throws InterruptedException {
        
    	Thread.sleep(4000);
    	driver.get(baseUrl);
    	// Open Incident table
    	jse = (JavascriptExecutor) driver;    
    	
    	//Navigation through all menu
    	navigator = new Navigator(driver);
    	navigator.allNavigation("incident.list", jse);

        // Search Incident
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(INCnumber + Keys.ENTER);

        Thread.sleep(2000);

        // Open incident
        List<WebElement> openINC = driver.findElements(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]"));
        for (WebElement ele2 : openINC) {
            String currentINC = ele2.getText();
            if (currentINC.contains(INCnumber)) {
                ele2.click();
                break;
            }
        }
    }
	

    @Test(description = "Verification of Adding comment on Incident", dependsOnMethods = "checkingIncident")
    public void addingComment() throws InterruptedException {

            // Add comment
            driver.findElement(By.xpath("//textarea[@id='activity-stream-textarea']")).sendKeys("Test");
            driver.findElement(By.xpath("//button[@class='btn btn-default activity-submit']")).click();
            Thread.sleep(2000);
            
            // Set Resolution Information
            // Set Resolution code
            WebElement dropdownElement = driver.findElement(By.id("//*[@id='incident.close_code']")); 
            
            // Locate the drop-down element
            Select dropdown = new Select(dropdownElement); // Create an instance of Select class
            dropdown.selectByVisibleText("Duplicate"); // Select option by visible text
            dropdown.selectByIndex(2); // Selects the second option (index starts from 0) // Select option by index
            dropdown.selectByValue("No resolution provided"); // Select option by value
            WebElement selectedOption = dropdown.getFirstSelectedOption(); // Get the selected option
            System.out.println("Selected option: " + selectedOption.getText());

            // Add resolution note
            driver.findElement(By.xpath("//*[@id='incident.close_notes']")).sendKeys("Testing comment");

            // Click on Resolve UI action
            driver.findElement(By.xpath("//*[@id='resolve_incident']")).click();

            System.out.println("Execution completed");
    }
}