package com.example.tests;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.testng.Assert;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import pages.LoginPage;
import utils.ExtentReportManager;

public class Incident_externization extends BaseTest {
	
	private WebDriver driver = DriverManager.getDriver();
	private JavascriptExecutor jse;

	
	public static Object[][] getData(String filePath, String sheetName) {
        Object[][] data = null;

        try {
            FileInputStream fis = new FileInputStream(filePath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            int rows = sheet.getPhysicalNumberOfRows();
            int cols = sheet.getRow(0).getLastCellNum();

            //System.out.println("rows : "+rows);
            //System.out.println("cols : "+cols);

            data = new Object[rows - 1][cols];

            for (int i = 1; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
                    //System.out.println("data "+ data[i - 1][j]);
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;        
    }


    @Test
    public void create_Incident () throws InterruptedException {
    	test = ExtentReportManager.createTest("Verification of Submitting Request Standing desk form");
        //String baseUrl = "https://dev336474.service-now.com/";

        // Create a object of getData method
        Object[][] users = getData("C:\\Users\\Sandesh Velhal\\eclipse-workspace\\Servicenow-AllModule\\src\\test\\resources\\Incident.xlsx", "Create_Inc");

        //
        for (int i = 0; i < users.length; i++) {

            String user = users[i][0].toString();
            String short_description = users[i][1].toString();

            // Open incident table
            driver.get(Config.baseUrl() + "/incident_list");
            jse = (JavascriptExecutor) driver;
            WebElement newButton = (WebElement) jse.executeScript("return document.querySelector(\"#sysverb_new\")");//document.querySelector("#sysverb_new")

            // Click on New button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", newButton);

            Thread.sleep(2000);
            // Copy Incident record number
            WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
            String value = inputElement.getAttribute("value");
            System.out.println(value);

            // Set caller value
            WebElement caller = driver.findElement(By.xpath("//*[@id='sys_display.incident.caller_id']"));
            caller.sendKeys( user + Keys.ENTER);

            Thread.sleep(2000);
            // Enter short description
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            driver.findElement(By.xpath("//*[@id='incident.short_description']")).sendKeys(short_description+ " " + timestamp);

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

            // full page screenshots  
            TakesScreenshot ts = (TakesScreenshot)driver;    
            File sourcefile = ts.getScreenshotAs(OutputType.FILE);

            // Generate timestamp
            String timestamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            //  Target file to store 
            File targetfile = new File(System.getProperty("user.dir")+"\\screenshots\\fullpage" + timestamp2 +".png");

            // copy sourcefile to targate file
            sourcefile.renameTo(targetfile);


            // Click on submit
            driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();

            Thread.sleep(2000);
            // Search Incident record on table
            WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
            globalSearchBox.sendKeys(value);
            Thread.sleep(1000);
            globalSearchBox.sendKeys(Keys.ENTER);

            Thread.sleep(2000);

            // Open incident
            List<WebElement> openINC = driver.findElements(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]"));
            for (WebElement ele2 : openINC) {
                String currentINC = ele2.getText();
                if (currentINC.contains(value)) {
                    ele2.click();
                    break;
                }
            }
        }

        System.out.println("Incident Craetion is complete");
        driver.quit();

    }


    @Test 
    public void validate_ErrorMessage_On_Incident () throws InterruptedException {

        String baseUrl = "https://dev336474.service-now.com/";

        Object[][] users = getData("C:\\Automation\\Demo\\src\\test\\resources\\Incident.xlsx", "Create_Inc");
        //System.out.println("users : "+users);

        driver.get(baseUrl);


        for (int i = 0; i < users.length; i++) {

            String user = users[i][0].toString();
            String short_description = users[i][1].toString();

            // Open incident table
            driver.get(Config.baseUrl() + "/incident_list");
            jse = (JavascriptExecutor) driver;
            WebElement newButton = (WebElement) jse.executeScript("return document.querySelector(\"#sysverb_new\")");//document.querySelector("#sysverb_new")

            // Click on New button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", newButton);

            Thread.sleep(2000);
            // Copy Incident record number
            WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
            String value = inputElement.getAttribute("value");
            System.out.println(value);

            // Click on submit
            driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
        
            // full page screenshots  

            TakesScreenshot ts = (TakesScreenshot)driver;    
            File sourcefile = ts.getScreenshotAs(OutputType.FILE);
            
            // Generate timestamp
            String timestamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            
            //  File targetfile = new File("C:\\Automation\\Demo\\screenshots\\fullpage"+timestamp +".png");
            File targetfile = new File(System.getProperty("user.dir")+"\\screenshots\\fullpage" + timestamp2 +".png");
            
            // copy sourcefile to targate file
            sourcefile.renameTo(targetfile);
            Thread.sleep(2000);

            // validate error message
            // Take error message from page
            WebElement errorElement = driver.findElement(By.xpath("//span[@class='outputmsg_text']"));
            String actualErrorMessage = driver.findElement(By.xpath("//span[@class='outputmsg_text']")).getText().trim();
            System.out.println("Actual Message : "+ actualErrorMessage);

            // Expected error message
            String expetcedErrorMessage = "The following mandatory fields are not filled in: Short description, Caller";

//            //Validate both are equal
//            if(expetcedErrorMessage.equals(actualErrorMessage)) {            
//                System.out.println("Error message validation is complete !!");
//                Assert.assertTrue(true);
//            }else {
//
//                System.out.println("Error message validation is failed !!");
//                Assert.assertTrue(false);
//            }
            Assert.assertTrue(errorElement.isDisplayed(), "Error message not displayed!");
//            
//            // Check if text matches 
            Assert.assertEquals(actualErrorMessage, expetcedErrorMessage, "Error message mismatch!");

            System.out.println("Error Message Validation is Complete");
            driver.quit();
        }
    }
}
