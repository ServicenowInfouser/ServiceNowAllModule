package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import utils.DataImport;
import utils.ExtentReportManager;

public class RequestRITMTask extends BaseTest {

	public WebDriver driver;
	private JavascriptExecutor jse;
	private WebDriverWait wait;

	String requestno, ritm, approver, catlogTask;
	private int TotalTask1, TotalTask2;

	// Import test data from "Request" sheet
	Object[][] requestData = DataImport.getData("Request");

	public RequestRITMTask(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.jse = (JavascriptExecutor) driver;
	}

	public void searchRequest(ExtentTest test, String requestno) {
		WebElement globalSearchBox = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//input[@class='form-control' and @type='search']")));
		globalSearchBox.sendKeys(requestno + Keys.ENTER);

		test.info("Search for the request", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Open Request
		test.info("Open Created request");
		List<WebElement> openReq = wait.until(ExpectedConditions
				.visibilityOfAllElementsLocatedBy(By.xpath("//table[@id='sc_request_table']/tbody/tr/td[3]/a")));
		for (WebElement ele2 : openReq) {
			if (ele2.getText().contains(requestno)) {
				ele2.click();
				break;
			}
		}
	}

	public String searchRITM(ExtentTest test) {
		// Open RITM
		test.info("Open RITM from the Requested Item Related List");
		ritm = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"))).getText();
		List<WebElement> openRITM = driver
				.findElements(By.xpath("//table[@id='sc_request.sc_req_item.request_table']/tbody/tr/td[3]/a"));
		for (WebElement ele2 : openRITM) {
			ele2.click();
		}
		return ritm;
	}

	// ===========================
	// Method: Create Request
	// ===========================
	public String createRequest(ExtentTest test) throws InterruptedException {
		// Navigate to Employee Center
		driver.get(BaseTest.baseUrl + "/esc");

		test.info("Navigating the Employee Center", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Search Catalog Item
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/input")))
				.sendKeys("Big Data Analysis");
		driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/span/button")).click();

		// Open catalog form
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//mark[normalize-space()='Big Data Analysis']")))
				.click();

		test.info("Opening catalog form", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Scroll to required fields
		WebElement fields = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'Indicates required')]")));
		jse.executeScript("arguments[0].scrollIntoView();", fields);

		// Enter Dynamic Data
		test.info("Enter Dynamic Data");
		System.out.println("Select User");
		driver.findElement(By.xpath("//div[@id='s2id_sp_formfield_primary_contact']//b")).click();
		driver.findElement(By.id("s2id_autogen2_search")).sendKeys((String) requestData[0][0]);
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//div[normalize-space()='" + (String) requestData[0][0] + "']")))
				.click();

		System.out.println("Select Cost Center");
		driver.findElement(By.id("s2id_sp_formfield_cost_center")).click();
		driver.findElement(By.id("s2id_autogen3_search")).sendKeys((String) requestData[0][1]);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(), 'Development')]"))).click();

		System.out.println("Select Business Purpose");
		String businessPurpose = "//span[normalize-space()='" + (String) requestData[0][2] + "']";
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(businessPurpose))).click();

		System.out.println("Enter description");
		driver.findElement(By.id("sp_formfield_needs")).sendKeys((String) requestData[0][3]);

		// Submit request
		System.out.println("Click on Submit");
		driver.findElement(By.id("submit-btn")).click();
		System.out.println("Select Checkout");
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"sc_cat_checkout\"]/div[3]/div/div/button[2]/span[1]")))
				.click();

		// Capture Request Number
		requestno = wait
				.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[@id=\"x6fb0f4029f8332002528d4b4232e70f6\"]/div[2]/div[2]/div/div[2]/b")))
				.getText();
		System.out.println("Request No : " + requestno);

		test.pass(requestno + " Request Created Successfully", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Custom report
		Reporter.getCurrentTestResult().setAttribute("TestData", requestno);
		return requestno;
	}

	// ===========================
	// Method: Check Request and RITM
	// ===========================
	public String checkReqRITM(ExtentTest test, String requestno) throws InterruptedException {
		// Navigating to Request List
		test.info("Navigating to Request List view");
		driver.get(BaseTest.baseUrl + "/sc_request_list");

		// Search Request
		test.info("Searching for the created Request : " + requestno);

		searchRequest(test, requestno);

		test.info("Opening the Request record", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Search and open RITM
		searchRITM(test);

		test.info("Opening the RITM record", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		test.info("RITM: " + ritm);
		test.pass("Request and RITM opened Successfully");

		// Custom report
		Reporter.getCurrentTestResult().setAttribute("TestData", requestno + " " + ritm);
		return ritm;
	}

	// ===========================
	// Method: Check Approvals
	// ===========================
	public String checkApprovals(ExtentTest test) throws InterruptedException {
		// Scroll down to related list
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);
		driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[2]/span/span[2]")).click();

		List<WebElement> fetchapprovers = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//table[@id='sc_req_item.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]")));
		System.out.println(fetchapprovers.size() + " Approver is generated");

		// Selecting approver
		if (!fetchapprovers.isEmpty()) {
			approver = fetchapprovers.get(0).getText();
		}
		System.out.println("Approver is : " + approver);
		test.info("Approver is : " + approver);

		test.pass("Generated approvals", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Custom report
		Reporter.getCurrentTestResult().setAttribute("TestData", approver);
		return approver;
	}

	// ===========================
	// Method: Check Catalog Task
	// ===========================
	public String checkCatalogTask(ExtentTest test, String requestno) throws InterruptedException {
		driver.get(BaseTest.baseUrl + "/sc_request_list");
		System.out.println(requestno);

		// Search Request
		searchRequest(test, requestno);

		// test.info("Opening the Request record",
		// MediaEntityBuilder.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		// Search and open RITM
		searchRITM(test);

		// SCTask verification
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL, Keys.END);

		// Navigate to Catalog Task tab
		System.out.println("navigate to SCTask tab");
		driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[1]/span/span[2]")).click();

		// Open the Catalog item from related list
		List<WebElement> catTask = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
				By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]")));
		System.out.println("Total Tasks generated : " + catTask.size());
		TotalTask1 = catTask.size();
		for (WebElement cat1 : catTask) {
			catlogTask = cat1.getText();
			System.out.println(cat1.getText());
			cat1.click();
		}

		test.info("Task Details", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		test.pass("Catalog Task is generated " + catlogTask);

		Reporter.getCurrentTestResult().setAttribute("TestData", catlogTask);
		return approver;
	}

	// ===========================
	// Method: Close Complete Catalog Task and Verify States
	// ===========================
	public void closeCompleteCatalogTask(ExtentTest test, String catlogTask) throws InterruptedException {
		WebElement taskState = driver.findElement(By.id("sc_task.state"));
		Select changeState = new Select(taskState);
		changeState.selectByVisibleText("Closed Complete");
		System.out.println("Task State changed to Closed Complete");
		driver.findElement(By.id("sysverb_update")).click();

		driver.navigate().refresh();

		List<WebElement> catTask2 = driver
				.findElements(By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]"));
		System.out.println("Total Tasks generated : " + catTask2.size());
		TotalTask2 = catTask2.size();

		Assert.assertEquals(TotalTask2, TotalTask1, "No more than 1 tasks are generated.");

		// Verify RITM State
		WebElement ritmState = driver.findElement(By.id("sc_req_item.state"));
		Select selectRitmState = new Select(ritmState);
		WebElement selectedRitmState = selectRitmState.getFirstSelectedOption();

		test.info("RITM State is " + selectedRitmState.getText());
		test.info("RITM State", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Assert.assertEquals(selectedRitmState.getText().trim(), "Closed Complete", "Test failed for " + ritm);

		// Verify Request State
		for (int i = 0; i < 3; i++) {
			driver.navigate().back();
			driver.navigate().refresh();
		}

		WebElement requestState = driver.findElement(By.id("sc_request.state"));
		Select selectReqState = new Select(requestState);
		WebElement selectedReqState = selectReqState.getFirstSelectedOption();

		test.info("Request State is " + selectedReqState.getText());
		test.info("Request State", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Assert.assertEquals(selectedReqState.getText().trim(), "Closed Complete", "Test failed for " + ritm);

		test.pass("Request, RITM States are Closed Completed");
	}

	// ===========================
	// Method: Close Incomplete Catalog Task and Verify States
	// ===========================
	public void closeIncompleteCatalogTask(ExtentTest test, String catlogTask) throws InterruptedException {
		// Closed Incomplete SCTask
		WebElement taskState = driver.findElement(By.id("sc_task.state"));
		Select changeState = new Select(taskState);
		changeState.selectByVisibleText("Closed Incomplete");
		System.out.println("Task State changed to Closed Incomplete");
		driver.findElement(By.id("sysverb_update")).click();

		Thread.sleep(2000);
		System.out.println("RITM opened :" + driver.getTitle());
		driver.navigate().refresh();

		List<WebElement> catTask2 = driver
				.findElements(By.xpath("//table[@id='sc_req_item.sc_task.request_item_table']/tbody/tr/td[3]"));
		Thread.sleep(2000);
		System.out.println("Total Tasks generated : " + catTask2.size());
		TotalTask2 = catTask2.size();

		Assert.assertEquals(TotalTask2, TotalTask1, "No more than 1 tasks are generated.");

		// Verify RITM State
		WebElement ritmState = driver.findElement(By.id("sc_req_item.state"));
		Select selectRitmState = new Select(ritmState);
		WebElement selectedRitmState = selectRitmState.getFirstSelectedOption();

		test.info("RITM State is " + selectedRitmState.getText());
		System.out.println("RITM State is : " + selectedRitmState.getText());

		test.info("RITM State", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Assert.assertEquals(selectedRitmState.getText().trim(), "Closed Incomplete", "Test failed for " + ritm);

		// Verify Request State
		for (int i = 0; i < 3; i++) {
			driver.navigate().back();
			driver.navigate().refresh();
			Thread.sleep(2000);
		}

		WebElement requestState = driver.findElement(By.id("sc_request.state"));
		Select selectReqState = new Select(requestState);
		WebElement selectedReqState = selectReqState.getFirstSelectedOption();

		test.info("Request State is " + selectedReqState.getText());
		System.out.println("REQUEST State is : " + selectedReqState.getText());

		test.info("Request State", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Assert.assertEquals(selectedReqState.getText().trim(), "Closed Incomplete", "Test failed for " + ritm);

		test.pass("Request, RITM States are Closed Incomplete");
	}
}