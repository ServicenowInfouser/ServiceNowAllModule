package base;

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
import utils.ExtentReportManager;
import utils.Log;

public class BaseTest {
	public static String baseUrl = Config.baseUrl();
	protected WebDriver driver ;
	protected static ExtentReports extent;
	protected ExtentTest test;
	private LoginPage loginPage;
	
	@BeforeSuite
	public void setupReport() throws InterruptedException {
		extent = ExtentReportManager.getReportInstance();
		
		driver = DriverManager.getDriver(); 
		loginPage = new LoginPage(driver); 
		
		loginPage.login(Config.username(), Config.password());
	}
	
	@AfterSuite
	public void teardownReport() {
		extent.flush();
		
		DriverManager.quitDriver();
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
			
			String screenshotPath = ExtentReportManager.captureScreenshot(driver, "LoginFailure");
			test.fail("Test Failed.. Check Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}
		
//		if (driver != null) {
//			Log.info("Closing Browser...");
//			driver.quit();
//		}
	}

}
