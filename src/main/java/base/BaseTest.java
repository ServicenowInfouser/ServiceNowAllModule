package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import pages.LoginPage;
import utils.DataImport;
import utils.ExtentReportManager;
import utils.Log;

public class BaseTest {
	public static String baseUrl = Config.baseUrl();
	public WebDriver driver ;
	protected static ExtentReports extent;
	protected ExtentTest test;
	
	private LoginPage loginPage;
	private Impersonation impersonation;
	
	@BeforeSuite
	public void setupReport() throws InterruptedException {
		extent = ExtentReportManager.getReportInstance();
		
		driver = DriverManager.getDriver(); 
		loginPage = new LoginPage(driver); 
		
		loginPage.login(Config.username(), Config.password());
		
//		//User Impersonation
//		JavascriptExecutor jse = (JavascriptExecutor) driver;
//    	test = ExtentReportManager.createTest("Verification of user Impersonation");
//    	
//		// Create a object of getData method
//        Object[][] users = DataImport.getData("ImpersonateUser");
//
//        //
//        for (int i = 0; i < users.length; i++) {
//
//            String user = users[i][0].toString();
//	    	test.info("Impersonate " +user);
//	    	impersonation = new Impersonation(driver);
//	    	impersonation.startImpersonation(user, jse);
//	    	test.pass("success");
//        }
	}
	
	@AfterSuite
	public void teardownReport() throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
    	
//		test = ExtentReportManager.createTest("Verification of End Impersonation");
//    	test.info("End Impersonation");
//    	impersonation = new Impersonation(driver);
//    	impersonation.endImpersonation(jse);
//    	test.pass("success");
    	
    	//Closing the driver
    	DriverManager.quitDriver();
		
		extent.flush();
		
		//String reportPath = ExtentReportManager.reportPath;
		//EmailUtils.sendTestReport(reportPath);
	}
	
	//@BeforeMethod
	public void setUp() {
		
		Log.info("Starting WebDriver...");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		Log.info("Navigating to URL...");
		driver.get("https://admin-demo.nopcommerce.com/login");
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) {
		
		if(result.getStatus() == ITestResult.FAILURE) {
			
			String screenshotPath = ExtentReportManager.captureScreenshot(driver, "fail");
			test.fail("Test Failed.. Check Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}
		
//		if (driver != null) {
//			Log.info("Closing Browser...");
//			driver.quit();
//		}
	}

}
