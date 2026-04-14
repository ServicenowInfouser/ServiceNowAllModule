package base;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import pages.LoginPage;
import utils.DataImport;
import utils.ExtentReportManager;

public class BaseTest {
	public static String baseUrl = Config.baseUrl();
	private WebDriver driver; 
	protected static ExtentReports extent;
	protected static ExtentTest test,test1;
	private JavascriptExecutor jse;
	
	private LoginPage loginPage;
	private Impersonation impersonation;
	
	// Test data (will be loaded in setup to handle errors gracefully)
	Object[][] users = null;
	
	@BeforeSuite
	public void setupReport() throws InterruptedException {
		extent = ExtentReportManager.getReportInstance();
		
		// Initialize WebDriver here so we can handle failures during setup
		driver = DriverManager.getDriver(); 
		if (driver == null) {
			throw new IllegalStateException("WebDriver instance is null. Cannot continue tests.");
		}
		
		// Add implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		loginPage = new LoginPage(driver); 
		driver.get(baseUrl);
		
		// Perform login
		loginPage.login(Config.username(), Config.password());
		
		// Load test data for impersonation and validate
		try {
			users = DataImport.getData("ImpersonateUser");
		} catch (Exception e) {
			System.out.println("BaseTest.setupReport: error loading ImpersonateUser data: " + e.getMessage());
			users = new Object[0][0];
		}
		
		if (users == null || users.length == 0 || users[0] == null || users[0].length == 0 || users[0][0] == null || users[0][0].toString().trim().isEmpty()) {
			System.out.println("BaseTest.setupReport: No impersonation user configured in test data. Skipping impersonation.");
			return; // still proceed without impersonation; consider failing fast if required
		}
		
		String user = users[0][0].toString();
		
		jse = (JavascriptExecutor) driver;
		impersonation = new Impersonation(driver);
		try {
			impersonation.startImpersonation(user, jse);
		} catch (Exception e) {
			System.out.println("BaseTest.setupReport: failed to start impersonation for user '" + user + "': " + e.getMessage());
		}
	}
	
	@AfterSuite
	public void teardownReport() throws InterruptedException {
		// End impersonation if it was started
		try {
			if (impersonation != null && jse != null) {
				impersonation.endImpersonation(jse);
			}
		} catch (Exception e) {
			System.out.println("BaseTest.teardownReport: error ending impersonation: " + e.getMessage());
		}
		
		// Closing the driver
		try {
			DriverManager.quitDriver();
		} catch (Exception e) {
			System.out.println("BaseTest.teardownReport: error quitting driver: " + e.getMessage());
		}
		
		if (extent != null) {
			extent.flush();
		}
	}
	
	//@BeforeClass
	public void impAdmin() throws InterruptedException {
		if (users == null || users.length == 0) {
			System.out.println("impAdmin: no impersonation users available.");
			return;
		}
		String user = users[0][0].toString();
		impersonation = new Impersonation(driver);
	    impersonation.startImpersonation(user, jse);
	}
	
	//@AfterClass
	public void endImpAdmin() throws InterruptedException {
		if (impersonation == null) return;
		JavascriptExecutor jseLocal = (JavascriptExecutor) driver;
		impersonation = new Impersonation(driver);
	    impersonation.endImpersonation(jseLocal);
	}
	
	@AfterMethod(alwaysRun = true)
	public ExtentTest tearDown(ITestResult result) {
		if(result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.fail("Test Failed.. Check Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}
		return test;
	}
}