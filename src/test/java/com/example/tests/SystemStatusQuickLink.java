
package com.example.tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.DriverManager;
import pages.LoginPage;
import utils.ExtentReportManager;

public class SystemStatusQuickLink extends BaseTest{	
	
	private WebDriver driver = DriverManager.getDriver();; 
	String requestno;

    @Test(description = "Verification of System Status Quick Link")
    public void clickQuicklinks() {
        
    	driver.get(baseUrl + "/esc");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        System.out.println(driver.getTitle());
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.xpath("//*[@id=\"a98711ea53331210b8f2ddeeff7b12ba-item-2\"]")).click();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        System.out.println(driver.getTitle());
    }
    
	@Test(description = "Verification of Subscribe to update button", dependsOnMethods = "clickQuicklinks")
    public void subscribeToUpdate() throws InterruptedException {
        
       //Verification of page
       WebElement element = driver.findElement(By.xpath("//*[@id=\"xa2bad482d7001200a9addd173e24d439\"]/div/div[1]/h3"));
       System.out.println(element);
       
       //Click on any one of Services
       WebElement link = driver.findElement(By.xpath("//*[@id=\"x4f6aaa42d7330200a9addd173e24d435\"]/div/div[2]/table/tbody/tr[2]/th/a"));
       link.click();
       Thread.sleep(4000);
       
       //Click on Subscribe to Updates button
       WebElement Subscribebutton=driver.findElement(By.xpath("//*[@id=\"x903a8614d7111200a9addd173e24d407\"]/div/div[2]/div/button"));
       System.out.println(Subscribebutton);
       Subscribebutton.click();
      
    }
	
	@Test(description = "Verification of UnSubscribe to update button", dependsOnMethods = "subscribeToUpdate")
    public void unSubscribeToUpdate() throws InterruptedException {
        
       Thread.sleep(4000);
       
       //Verification of Unsubscribe to Updates button
       WebElement Unsubscribebutton = driver.findElement(By.xpath("//*[@id=\"x903a8614d7111200a9addd173e24d407\"]/div/div[2]/div/button"));
       System.out.println(Unsubscribebutton);
       Unsubscribebutton.click();
       Thread.sleep(4000);
    }
}