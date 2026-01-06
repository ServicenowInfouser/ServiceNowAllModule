package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import utils.DataImport;
import utils.ExtentReportManager;

public class RequestRITMTask extends BaseTest {
	
	public WebDriver driver;
	
	private JavascriptExecutor jse;
	
	String requestno, ritm, approver;
	
	Object[][] requestData = DataImport.getData("Request");
	
	
	public RequestRITMTask(WebDriver driver) {
        this.driver = driver;
    }
	
	public String CreateRequest(ExtentTest test) throws InterruptedException {
		
		try {
			test = ExtentReportManager.createTest("Verification of Submitting the ig Data Analysis catalog item");
			driver.get(BaseTest.baseUrl + "/esc");
			Thread.sleep(3000);
			String CatalogName = "Big Data Analysis";
			driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/input")).sendKeys(CatalogName);
			driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/span/button")).click();
			Thread.sleep(5000);
			test.info("Opening catalog form");
			driver.findElement(By.xpath("//mark[normalize-space()='Big Data Analysis']")).click();
			Thread.sleep(3000);
			System.out.println(CatalogName + " Form is visible");
			jse = (JavascriptExecutor) driver;
			//jse.executeScript("window.scrollBy(0,1500)");
			Thread.sleep(5000);
			WebElement fields = driver.findElement(By.xpath("// *[contains(text(),'Indicates required')]"));
			jse.executeScript("arguments[0].scrollIntoView();", fields);
			Thread.sleep(2000);
			test.info("Enter Dynamic Data");
			System.out.println("Select User");
			driver.findElement(By.xpath("//div[@id='s2id_sp_formfield_primary_contact']//b")).click();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.findElement(By.xpath("//*[@id=\"s2id_autogen2_search\"]"))
					.sendKeys((CharSequence[]) requestData[0][0]);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.findElement(By.xpath("//div[normalize-space()='Andrew Jackson']")).click();
			Thread.sleep(2000);
			System.out.println("Select Cost Center");
			driver.findElement(By.xpath("//*[@id=\"s2id_sp_formfield_cost_center\"]/a")).click();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.findElement(By.xpath("//input[@id='s2id_autogen3_search']"))
					.sendKeys((CharSequence[]) requestData[0][1]);
			//Select selectValu=new Select(element);
			//selectValu.selectByVisibleText("Development");
			driver.findElement(By.xpath("//div[@id=\"select2-drop\"]/ul/li/div[contains(text(), 'Development')]"))
					.click();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			Thread.sleep(2000);
			System.out.println("Select Business Purpose");
			String businessPurpose = "//span[normalize-space()='" + (CharSequence[]) requestData[0][2] + "']";
			driver.findElement(By.xpath(businessPurpose)).click();
			Thread.sleep(2000);
			System.out.println("Enter description");
			driver.findElement(By.xpath("//*[@id=\"sp_formfield_needs\"]"))
					.sendKeys((CharSequence[]) requestData[0][3]);
			Thread.sleep(2000);
			System.out.println("Click on Submit");
			driver.findElement(By.xpath("//*[@id=\"submit-btn\"]")).click();
			System.out.println("Select Checkout");
			driver.findElement(By.xpath("//*[@id=\"sc_cat_checkout\"]/div[3]/div/div/button[2]/span[1]")).click();
			Thread.sleep(3000);
			requestno = driver
					.findElement(By.xpath("//*[@id=\"x6fb0f4029f8332002528d4b4232e70f6\"]/div[2]/div[2]/div/div[2]/b"))
					.getText();
			System.out.println("Request No : " + requestno);
			test.pass(requestno + " Request Created Successfully");
		} finally {
			// TODO: handle finally clause
			return requestno;
		}
		
	}
	
	public String CheckReqRITM(ExtentTest test, String requestno) throws InterruptedException {
		
		test = ExtentReportManager.createTest("SC_002- Verification of Submitted Request and RITM");
		
		//Navigating to Request List
		test.info("Navigating to Request List view");
		driver.get(BaseTest.baseUrl + "/sc_request_list");
		System.out.println(requestno);
		Thread.sleep(2000);
		        
		// Search Request
		test.info("Searching for the created Request " + requestno);
		        //WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @typ='search']"));
		        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		        globalSearchBox.sendKeys(requestno + Keys.ENTER);
		        String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		    	test.info("Search generated request", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
		        
		        Thread.sleep(2000);
		    
		        // Open Request
		        test.info("Open Created request");
		        List<WebElement> openReq = driver.findElements(By.xpath("//table[@id='sc_request_table']/tbody/tr/td[3]/a"));
		        for (WebElement ele2 : openReq) {
		            String currentReq = ele2.getText();
		            if (currentReq.contains(requestno)) {
		                ele2.click();
		                break;
		            }
		        }
		        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		    	test.info("Navigating to Request", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
		        
		        // Attach custom test data to report
		        //Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
		        System.out.println(driver.getTitle());
		        Thread.sleep(2000);

				//Open RITM
				test.info("Open RITM from the Requested Item Related List");
				ritm = driver.findElement(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a")).getText();
		        List<WebElement> openRITM = driver.findElements(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"));
		        for (WebElement ele2 : openRITM) {
		            ele2.click();  
		        }
		        //System.out.print("Primary Contact "+driver.findElement(By.xpath("//*[@id=\"element.ni.VEe894cf7c839ef250f4aac629feaad335\"]/td/div/div/div/div[2]/div")).getText());
		        
		        
		        String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		        test.info("Navigating to RITM", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
		        System.out.println(driver.getTitle());
		        Thread.sleep(3000);
		        
		        test.info("RITM: "+ritm);
		        
		        test.pass("Request and RITM opened Successfully");
		        
		        //Custom report
		        Reporter.getCurrentTestResult().setAttribute("TestData", requestno+" "+ritm);
		        
		        return ritm;
	}
	
	public String checkApprovals(ExtentTest test) throws InterruptedException {
		//Scroll down to related list
		test = ExtentReportManager.createTest("SC_003- Verification of Generated Approvals");
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
        
        driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[2]/span/span[2]")).click();
        
        List<WebElement> fetchapprovers = driver.findElements(By.xpath("//table[@id='sc_req_item.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println(fetchapprovers.size()+" Approver is generated");
        
        //Selecting approver
        if (!fetchapprovers.isEmpty()) {
            approver = fetchapprovers.get(0).getText();
        }
        System.out.println("Approver is : " +approver);
        test.info("Approver is : " +approver);
        
        String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
        test.pass("Generated approvals", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
        
        //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", approver);
        
        return approver;
	}

}
