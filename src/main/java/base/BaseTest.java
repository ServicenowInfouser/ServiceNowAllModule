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
	private WebDriver driver = DriverManager.getDriver(); 
	protected static ExtentReports extent;
	protected ExtentTest test,test1;
	private JavascriptExecutor jse = (JavascriptExecutor) driver;
	
	private LoginPage loginPage;
	private Impersonation impersonation;
	
	// Test data
    Object[][] users = DataImport.getData("ImpersonateUser");
	
	@BeforeSuite
	public void setupReport() throws InterruptedException {
		extent = ExtentReportManager.getReportInstance();
		
		driver = DriverManager.getDriver(); 
		
		// ✅ Add implicit wait 
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		loginPage = new LoginPage(driver); 
		driver.get(baseUrl);
		
		loginPage.login(Config.username(), Config.password());
		
		//User Impersonation
		String user = users[0][0].toString();
		impersonation = new Impersonation(driver);
	    impersonation.startImpersonation("Fred Luddy", jse);
	}
	
	@AfterSuite
	public void teardownReport() throws InterruptedException {
		jse = (JavascriptExecutor) driver;
		impersonation = new Impersonation(driver);
    	impersonation.endImpersonation(jse);
    	
    	//Closing the driver
    	DriverManager.quitDriver();
		
		extent.flush();
		
		//String reportPath = ExtentReportManager.reportPath;
		//EmailUtils.sendTestReport(reportPath);
	}
	
	//@BeforeClass
	public void impAdmin() throws InterruptedException {
		String user = users[0][0].toString();
		impersonation = new Impersonation(driver);
    	impersonation.startImpersonation(user, jse);
	}
	
	//@AfterClass
	public void endImpAdmin() throws InterruptedException {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		impersonation = new Impersonation(driver);
    	impersonation.endImpersonation(jse);
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
