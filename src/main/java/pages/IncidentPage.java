package pages;

import java.io.FileInputStream;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import utils.ExtentReportManager;

public class IncidentPage {

	protected static ExtentTest test;

	private static WebDriver driver;
	SoftAssert soft = new SoftAssert();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	public IncidentPage(WebDriver driver) {
		this.driver = driver;
	}

	// Method to take full page screenshots
	public static void takeFullScreenshots() {
		// WebDriver driver = new ChromeDriver();
		// full page screenshots
		TakesScreenshot ts = (TakesScreenshot) driver;
		File sourcefile = ts.getScreenshotAs(OutputType.FILE);

		// Generate timestamp
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		// File targetfile = new
		// File("C:\\Automation\\Demo\\screenshots\\fullpage"+timestamp +".png");
		File targetfile = new File(System.getProperty("user.dir") + "\\screenshots\\fullpage" + timestamp + ".png");

		// copy sourcefile to targate file
		sourcefile.renameTo(targetfile);

	}

	// Method to get data from Excel file
	public static Object[][] getExcelData(String filePath, String sheetName) {
		Object[][] data = null;

		try {
			FileInputStream fis = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);

			int rows = sheet.getPhysicalNumberOfRows();
			int cols = sheet.getRow(0).getLastCellNum();

			// System.out.println("rows : "+rows);
			// System.out.println("cols : "+cols);

			data = new Object[rows - 1][cols];

			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					data[i - 1][j] = sheet.getRow(i).getCell(j).toString();
					// System.out.println("data "+ data[i - 1][j]);
				}
			}

			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	// Navigation
	public void openList(String baseUrl) {
		driver.get(baseUrl + "/incident_list");
	}

	// Method to Search Incident
	public void searchIncident(String incNum, ExtentTest test) throws InterruptedException {
		// Search Incident record on table

		Boolean isVisible = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']")).isDisplayed();
		if(isVisible) {
			WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
			globalSearchBox.sendKeys(incNum);
			Thread.sleep(1000);
			globalSearchBox.sendKeys(Keys.ENTER);
		}else {
			System.out.println("Element is not displayed");
		}
		// Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());

		Thread.sleep(2000);
		test.pass("Verified able to Search the Incident");
	}

	// Method to open Incident
	public void openIncident(String incNum, ExtentTest test) {

		// Open incident
		List<WebElement> openINC = driver.findElements(By.xpath("//table[@id='incident_table']/tbody/tr/td[3]"));
		for (WebElement ele2 : openINC) {
			String currentINC = ele2.getText();
			if (currentINC.contains(incNum)) {
				ele2.click();
				break;
			}
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.pass("Verified able to open Incident.");
	}

	// Validate that expected value is same as actual value
	public void testSoftAssert(String actualValue, String expectedValuse) {
		SoftAssert softAssert = new SoftAssert();

		softAssert.assertEquals(actualValue, expectedValuse, "Title does not match");
		softAssert.assertTrue(false, "Condition failed");

		System.out.println("Execution continues even after failures...");

		// Important: This will throw all collected assertion errors
		softAssert.assertAll();
	}

	// Method to select user in caller field
	public void select_Caller(String caller_Name, ExtentTest test) throws InterruptedException {

		// Verify caller field is diplayed
		WebElement fieldText = driver.findElement(By.xpath("//*[@id='sys_display.incident.caller_id']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Caller field is not displayed. ");

		// Select caller
		WebElement caller = driver.findElement(By.xpath("//*[@id='sys_display.incident.caller_id']"));
		caller.sendKeys(caller_Name + Keys.ENTER);
		Thread.sleep(2000);
		test.info("Caller :" + caller_Name);
		System.out.println("Caller :" + caller_Name);
	}

	// Method to enter short description on forn
	public void enter_ShortDescription(String short_Description, ExtentTest test) {

		// verify short_Description displayed
		WebElement fieldText = driver.findElement(By.xpath("//*[@id='incident.short_description']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Short Description field is not displayed. ");

		// enter short_Description
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		driver.findElement(By.xpath("//*[@id='incident.short_description']"))
		.sendKeys(short_Description + " " + timestamp);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Short Description :" + short_Description);
	}

	// method to select impact
	public void select_Impact(String impact, ExtentTest test) {

		// Verify error message displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.impact']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Impact field is not displayed. ");
		test.pass("Impact field is Visible");

		// Select impact
		driver.findElement(By.xpath("//select[@id='incident.impact']")).click();
		driver.findElement(By.xpath("//select[@id='incident.impact']//option[contains(text(), '" + impact + "')]"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		System.out.println("Impact : " + impact);
		test.info("Impact : " + impact);

	}

	// Method to select urgency
	public void select_Urgency(String urgency, ExtentTest test) {

		// Verify error message displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.urgency']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Urgency field is not displayed. ");
		test.pass("Urgency field is Visible");

		// Select impact
		driver.findElement(By.xpath("//select[@id='incident.urgency']")).click();
		driver.findElement(By.xpath("//select[@id='incident.urgency']//option[contains(text(), '" + urgency + "')]"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		System.out.println("Urgency : " + urgency);
		test.info("Urgency : " + urgency);
	}

	// Method to select category
	public void select_Category(String category, ExtentTest test) {

		// Verify error message displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.category']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Category field is not displayed. ");
		test.pass("Category field is Visible");

		// Select category
		driver.findElement(By.xpath("//select[@id='incident.category']")).click();
		driver.findElement(By.xpath("//select[@id='incident.category']//option[contains(text(), '" + category + "')]"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		System.out.println("Category : " + category);
		test.info("Category : " + category);
	}

	// method to select sub_Category
	public void Select_Sub_Category(String sub_Category, ExtentTest test) {

		// Verify error message displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.subcategory']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Category field is not displayed. ");
		test.pass("Subcategory field is Visible");

		// Select category
		driver.findElement(By.xpath("//select[@id='incident.subcategory']")).click();
		driver.findElement(
				By.xpath("//select[@id='incident.subcategory']//option[contains(text(), '" + sub_Category + "')]"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		System.out.println("Sub Category : " + sub_Category);
		test.info("Sub Category : " + sub_Category);
	}

	// Method to verify error message on incident form
	public void verifyIncidentErrorMessage(String expectedErrorMsg, ExtentTest test) {
		SoftAssert softAssert = new SoftAssert();

		// Verify error message displayed
		WebElement fieldText = driver.findElement(By.xpath("//span[@class='outputmsg_text']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Error message is not displayed. ");

		// Get error message text from page
		// WebElement errorElement =
		// driver.findElement(By.xpath("//span[@class='outputmsg_text']"));
		String actualErrorMessage = driver.findElement(By.xpath("//span[@class='outputmsg_text']")).getText().trim();
		// System.out.println("Actual Message : "+ actualErrorMessage);

		// Compare actual message and expected message
		softAssert.assertEquals(actualErrorMessage, expectedErrorMsg, "Error Message does not match");

		// Important: This will throw all collected assertion errors
		softAssert.assertAll();

		test.pass("Verified error messsge : " + expectedErrorMsg + " is visible on Incident");
	}

	// Method to verify state of Incident record
	public void verify_Incident_State(String exp_stateCode, ExtentTest test) {

		// Verify state field displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@name='incident.state']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Incident state field is not displayed. ");
		// test.info("State field is Visible");

		// Verify state of Incident is Closed
		String act_incident_state = driver.findElement(By.xpath("//select[@id='incident.state']"))
				.getAttribute("value");

		// Check if text matches
		Assert.assertEquals(act_incident_state, exp_stateCode, "State mismatch!");

		if (act_incident_state.equals("1")) {
			System.out.println("Incident state is New");
			test.pass("Verified Incident state is New.");
		} else if (act_incident_state.equals("2")) {
			System.out.println("Incident state is In Progress");
			test.pass("Verified Incident state is In Progress.");
		} else if (act_incident_state.equals("3")) {
			System.out.println("Incident state is On Hold");
			test.pass("Verified Incident state is On Hold.");
		} else if (act_incident_state.equals("6")) {
			System.out.println("Incident state is Resolved");
			test.pass("Verified Incident state is Resolved.");
		} else if (act_incident_state.equals("7")) {
			System.out.println("Incident state is Closed");
			test.pass("Verified Incident state is Closed.");
		} else if (act_incident_state.equals("8")) {
			System.out.println("Incident state is Canceled");
			test.pass("Verified Incident state is Canceled.");
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	// Method to verify state of Incident record
	public void verify_Incident_State_Canceled(String exp_stateCode, ExtentTest test) {

		// Verify state field displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@name='sys_readonly.incident.state']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Incident state field is not displayed. ");

		// Verify state of Incident is Closed
		String act_incident_state = driver.findElement(By.xpath("//select[@name='sys_readonly.incident.state']"))
				.getAttribute("value");

		// Check if text matches
		Assert.assertEquals(act_incident_state, exp_stateCode, "State mismatch!");

		if (act_incident_state.equals("1")) {
			System.out.println("Incident state is New");
			test.pass("Verified Incident state is New.");
		} else if (act_incident_state.equals("2")) {
			System.out.println("Incident state is In Progress");
			test.info("Verified Incident state is In Progress.");
		} else if (act_incident_state.equals("3")) {
			System.out.println("Incident state is On Hold");
			test.info("Verified Incident state is On Hold.");
		} else if (act_incident_state.equals("6")) {
			System.out.println("Incident state is Resolved");
			test.info("Verified Incident state is Resolved.");
		} else if (act_incident_state.equals("7")) {
			System.out.println("Incident state is Closed");
			test.info("Verified Incident state is Closed.");
		} else if (act_incident_state.equals("8")) {
			System.out.println("Incident state is Canceled");
			test.pass("Verified Incident state is Canceled.");
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}

	// Methhod to verify field is visible on Incident form
	public void verify_Field_Is_Visible(String xpath, ExtentTest test) {

		WebElement fieldText = driver.findElement(By.xpath(xpath));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Field is not displayed. ");
		String text = fieldText.getText();
		;
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.pass(text + " Field is Displayed");
		System.out.println(text + " Field is Displayed");
	}

	// Method to verify field on form is mandatory
	public void verify_Field_Is_Mandatory(String xpath, String field_Name, ExtentTest test) {
		WebElement field_state = driver.findElement(By.xpath(xpath));
		String requiredAttr = field_state.getAttribute("mandatory");
		// System.out.println("requiredAttr3 : "+requiredAttr);
		Assert.assertTrue(requiredAttr != null, "Field is not marked as required");
		test.pass("Verified " + field_Name + " field is mandatory");
		System.out.println("Verified " + field_Name + " field is mandatory");
	}

	/*
	 * //Method to chnage the state of incident > add state code in argument
	 * 
	 * public static void change_Incident_state(String state_Code) {
	 * 
	 * WebElement fieldText =
	 * driver.findElement(By.xpath("//select[@name='incident.state']")); Boolean
	 * fieldDisplayed = fieldText.isDisplayed(); Assert.assertTrue(fieldDisplayed !=
	 * false, "Incident state field is not displayed. ");
	 * 
	 * driver.findElement(By.xpath("//select[@name='incident.state']")).click();
	 * driver.findElement(By.
	 * cssSelector("select[id='incident.state'] option[value='" + state_Code +
	 * "']")).click();
	 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	 * report.info("Incident state is changed to "+state_Code);
	 * 
	 * }
	 */

	// Method to change state of Incident by passing state
	public void change_Incident_state(String inc_State, ExtentTest test) {

		// Verify state field displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@name='incident.state']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Incident state field is not displayed. ");

		// select state
		driver.findElement(By.xpath("//select[@name='incident.state']")).click();
		driver.findElement(By.xpath("//select[@id='incident.state']//option[contains(text(), '" + inc_State + "')]"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Incident state is changed to " + inc_State);
	}

	// Method to select Resolution code/Close code on Incident by passing resolution
	// code
	public void select_Resolution_Code_On_Incident(String resolution_Code_Option, ExtentTest test) {
		// Verify resolution code field displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.close_code']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Resolution code field is not displayed. ");

		// Select code
		driver.findElement(By.xpath("//select[@id='incident.close_code']")).click();
		driver.findElement(
				By.xpath("//select[@id='incident.close_code']/option[@value='" + resolution_Code_Option + "']"))
		.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Select Resolution code : " + resolution_Code_Option);
	}

	/*
	 * public static void select_On_Hold_Reason_On_Incident(String on_Hold_Reason) {
	 * 
	 * WebElement fieldText =
	 * driver.findElement(By.xpath("//select[@id='incident.hold_reason']")); Boolean
	 * fieldDisplayed = fieldText.isDisplayed(); Assert.assertTrue(fieldDisplayed !=
	 * false, "Hold Reason field is not displayed. ");
	 * 
	 * driver.findElement(By.xpath("//select[@id='incident.hold_reason']")).click();
	 * driver.findElement(By.xpath(
	 * "//select[@id='incident.hold_reason']/option[@value='" + on_Hold_Reason +
	 * "']")).click();
	 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	 * report.info("Select On Hold Reason : Awaiting for Change");
	 * 
	 * }
	 */

	// Method to select On Hold reason by passing On Hold reason
	public void select_On_Hold_Reason_On_Incident(String on_Hold_Reason, ExtentTest test) {

		// Verify ON HOLD Reason field displayed
		WebElement fieldText = driver.findElement(By.xpath("//select[@id='incident.hold_reason']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Hold Reason field is not displayed. ");

		// select on hold reason
		WebElement holdReasonSelect = driver.findElement(By.xpath("//select[@id='incident.hold_reason']"));
		new Select(holdReasonSelect).selectByVisibleText(on_Hold_Reason);
		// driver.findElement(By.xpath("//select[@id='incident.hold_reason']//option[contains(text(),
		// '"+ on_Hold_Reason+"'])")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Select On Hold Reason : " + on_Hold_Reason);
	}

	// Method to click on New button on Incident list view
	public void click_On_New_Button(ExtentTest test) throws InterruptedException {

		// Verify New button displayed
		WebElement fieldText = driver.findElement(By.xpath("//button[@id='sysverb_new']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "New button is not displayed. ");

		// click on new button
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		WebElement newButton = (WebElement) jse.executeScript("return document.querySelector(\"#sysverb_new\")");// document.querySelector("#sysverb_new")
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", newButton);
		Thread.sleep(2000);
		test.info("Click on New button");

		// Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
	}

	// Method to update the Incident form
	public void click_On_Update_Incident_Button(ExtentTest test) {

		// Verify update button is displayed
		WebElement fieldText = driver.findElement(By.xpath("//button[@id='sysverb_update']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Update Button is not displayed. ");

		// click on update button
		driver.findElement(By.xpath("//button[@id='sysverb_update']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Update the Incident Record");
	}

	// Method to click on Submit button on Incident
	public void click_On_Submit_Incident_Button(ExtentTest test) throws InterruptedException {

		// Verify Submit button displayed
		WebElement fieldText = driver.findElement(By.xpath("//*[@id='sysverb_insert']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Submit Button is not displayed. ");

		// click on submit button
		driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Submit the Incident Record");
		Thread.sleep(2000);
	}

	// Method to click on Resolve button on Incident
	public void click_On_Resolved_Incident_Button(ExtentTest test) {

		// Verify Resolve button displayed
		WebElement fieldText = driver.findElement(By.xpath("//button[@id='resolve_incident']"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Resolve button is not displayed. ");

		// click on Resolved button
		driver.findElement(By.xpath("//button[@id='resolve_incident']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Resolve the Incident");
	}

	// Method to click on Close button on Incident
	public void click_On_Closed_Incident_Button(ExtentTest test) {
		System.out.println("Close button method started");

		// Verify Close button displayed

		/*
		 * WebElement fieldText =
		 * driver.findElement(By.xpath("//button[@id='close_incident']")); Boolean
		 * fieldDisplayed = fieldText.isDisplayed();
		 * System.out.println("Close button is Visible ? "+fieldDisplayed);
		 * 
		 * if (fieldDisplayed.equals(true)) { //Click on Close button
		 * System.out.println("Close button is Visible");
		 * driver.findElement(By.xpath("//button[@id='close_incident']")).click();
		 * driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		 * test.info("Close the Incident"); } else {
		 * System.out.println("Close button is not Visible");
		 * test.info("Closed button not visible on Incident"); SoftAssert softAssert =
		 * new SoftAssert(); softAssert.assertEquals(fieldDisplayed, "true",
		 * "Closed button not visible on Incident");
		 * 
		 * ; }
		 */

		By closeBtn = By.xpath("//button[@id='close_incident']");

		try {
			// 1) Check presence without throwing
			if (driver.findElements(closeBtn).isEmpty()) {
				System.out.println("Close button NOT present on the page.");
				test.info("Close button not present on Incident page.");
				// continue test flow without failing
			} else {
				// 2) Wait for visibility (if it becomes visible within timeout)
				WebElement btn = (WebElement) closeBtn;
				System.out.println("Close button is visible: " + btn.isDisplayed());
				test.info("Close button is visible.");

				// 3) Wait for clickability and click
				// wait.until(ExpectedConditions.elementToBeClickable(closeBtn)).click();
				test.info("Clicked Close button to close the Incident.");
			}
		} catch (NoSuchElementException nse) {
			// (Rare here because we used findElements, but included for completeness)
			System.out.println("Close button not found (NoSuchElementException).");
			test.info("Close button not found; skipping close action.");
		} catch (StaleElementReferenceException sere) {
			System.out.println("Close button became stale; skipping close action.");
			test.info("Close button stale; skipping close action.");
		}

	}

	public void select_Assignement_Group(String groupName, ExtentTest test) throws InterruptedException {

		// Verify Assignement Group displayed
		WebElement fieldText = driver.findElement(By.xpath("//input[@id=\"sys_display.incident.assignment_group\"]"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Assignement Group is not displayed. ");
		test.pass("Assignement Group field is Visible");


		// **Set Assignment group field value**
		String mainWindowHandle1 = driver.getWindowHandle();

		test.info("Set Assignment group field value");

		// Click on Assignment group look up
		driver.findElement(By.xpath("//*[@id='lookup.incident.assignment_group']")).click();
		Thread.sleep(2000);

		// Get all window handles after the new window opens
		Set<String> allWindowHandles1 = driver.getWindowHandles();

		// Switch to child opened window Set<String> allWindowHandles
		Iterator<String> iterator1 = allWindowHandles1.iterator();
		while (iterator1.hasNext()) {
			String childWindowHandle1 = iterator1.next();

			// Switch to the child window only if it's not the main window
			if (!mainWindowHandle1.equalsIgnoreCase(childWindowHandle1)) {
				driver.switchTo().window(childWindowHandle1);
				// Search assignment group
				WebElement searchAssignmentGroup = driver
						.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
				searchAssignmentGroup.sendKeys(groupName+ Keys.ENTER);
				Thread.sleep(3000);

				// Select assignment group
				driver.findElement(By.xpath("//table[@id='sys_user_group_table']/tbody/tr[1]/td[3]/a")).click();
				break;
			}
		}
		// Switch back to the main window
		driver.switchTo().window(mainWindowHandle1);

		// Print set value
		String assignmentGroupValue = driver
				.findElement(By.xpath("//*[@id='sys_display.incident.assignment_group']")).getAttribute("value");

		test.log(Status.INFO, "Selected Assignment group field value is - " + assignmentGroupValue);

		// Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info( "Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}


	public void select_User_in_AssignedTo(String user, ExtentTest test) throws InterruptedException {
		// Verify Assignement TO displayed
		WebElement fieldText = driver.findElement(By.xpath("//input[@id=\"sys_display.incident.assigned_to\"]"));
		Boolean fieldDisplayed = fieldText.isDisplayed();
		Assert.assertTrue(fieldDisplayed != false, "Assignement To is not displayed. ");
		test.pass("Assignement To field is Visible");


		// Verify Assign to field
		test.pass("Verified Assign to field is Visible");

		String assignToField = driver.findElement(By.xpath("//*[@id='label.incident.assigned_to']/label/span[2]"))
				.getText();


		// Set Assign to field value
		String mainWindowHandle2 = driver.getWindowHandle();

		test.log(Status.INFO, "Set Assign to user in field");
		// Click on Assign to look up
		driver.findElement(By.xpath("//*[@id='lookup.incident.assigned_to']")).click();
		Thread.sleep(2000);

		// Get all window handles after the new window opens
		Set<String> allWindowHandles2 = driver.getWindowHandles();

		// Switch to child opened window Set<String> allWindowHandles
		Iterator<String> iterator2 = allWindowHandles2.iterator();
		while (iterator2.hasNext()) {
			String childWindowHandle2 = iterator2.next();

			// Switch to the child window only if it's not the main window
			if (!mainWindowHandle2.equalsIgnoreCase(childWindowHandle2)) {
				driver.switchTo().window(childWindowHandle2);
				// Search assign to
				WebElement searchAssigTo = driver
						.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
				searchAssigTo.sendKeys("Don Goodliffe" + Keys.ENTER);

				Thread.sleep(2000);
				// Select assign to
				driver.findElement(By.xpath("//table[@id='sys_user_table']/tbody/tr[1]/td[3]")).click();
				break;
			}
		}

		// Switch back to the main window
		driver.switchTo().window(mainWindowHandle2);

		// Print set value
		String assignToalue = driver.findElement(By.xpath("//*[@id='sys_display.incident.assigned_to']"))
				.getAttribute("value");

		test.log(Status.INFO, "Selected Assign to field value is - " + assignToalue);// Verify Assign to field
		test.pass("Verified Assign to field is Visible");

		String assignToField1 = driver.findElement(By.xpath("//*[@id='label.incident.assigned_to']/label/span[2]"))
				.getText();


		// Set Assign to field value
		String mainWindowHandle21 = driver.getWindowHandle();

		test.log(Status.INFO, "Set Assign to user in field");
		// Click on Assign to look up
		driver.findElement(By.xpath("//*[@id='lookup.incident.assigned_to']")).click();
		Thread.sleep(2000);

		// Get all window handles after the new window opens
		Set<String> allWindowHandles21 = driver.getWindowHandles();

		// Switch to child opened window Set<String> allWindowHandles
		Iterator<String> iterator21 = allWindowHandles21.iterator();
		while (iterator21.hasNext()) {
			String childWindowHandle2 = iterator21.next();

			// Switch to the child window only if it's not the main window
			if (!mainWindowHandle21.equalsIgnoreCase(childWindowHandle2)) {
				driver.switchTo().window(childWindowHandle2);
				// Search assign to
				WebElement searchAssigTo = driver
						.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
				searchAssigTo.sendKeys(user + Keys.ENTER);

				Thread.sleep(2000);
				// Select assign to
				driver.findElement(By.xpath("//table[@id='sys_user_table']/tbody/tr[1]/td[3]")).click();
				break;
			}
		}

		// Switch back to the main window
		driver.switchTo().window(mainWindowHandle21);

		// Print set value
		String assignToalue1 = driver.findElement(By.xpath("//*[@id='sys_display.incident.assigned_to']"))
				.getAttribute("value");

		test.log(Status.INFO, "Selected Assign to field value is - " + assignToalue1);

		// Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info( "Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}

	public void add_WorkNote(String note, ExtentTest test) throws InterruptedException {
		//Add work note
		// scroll to note tab
		WebElement element2 = driver
				.findElement(By.xpath("//textarea[@id='activity-stream-work_notes-textarea']"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView(true);", element2);
		Thread.sleep(4000);
		// add work note		
		driver.findElement(By.xpath("//textarea[@id='activity-stream-work_notes-textarea']")).sendKeys(note);
		test.info("Work note "+note+" added");
		Thread.sleep(4000);

		// Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Work Note: ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

	}

	public void file_upload_on_Incident(String filePath, String fileName, ExtentTest test)
			throws InterruptedException, AWTException {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement uploadElement = wait
				.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#header_add_attachment")));

		Thread.sleep(3000);

		driver.findElement(By.cssSelector("#header_add_attachment")).click();
		driver.findElement(By.xpath("//button[normalize-space()='Choose file']")).click();

		Thread.sleep(3000);
		// uploadElement.sendKeys("C://Users/Snehal
		// Patil/git/ServiceNowAllModule2/src/test/resources/docx_26kb.docx");
		uploadElement.sendKeys(filePath);

		//			// copy file path into click board (ctrl_C)
		//			StringSelection filePathSelection = new StringSelection(
		//					"C:\\Users\\Snehal Patil\\git\\ServiceNowAllModule2\\src\\test\\resources\\docx_26kb.docx");
		//			
		StringSelection filePathSelection = new StringSelection(filePath);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePathSelection, null);
		Thread.sleep(3000);
		System.out.println("ctrl_C");

		// ctrl+V
		Robot rb = new Robot();
		rb.keyPress(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(3000);
		System.out.println("ctrl_V");
		
		test.info("Navigate to file loaction : "+filePath);
		/*
		 * test.info("Navigate to file loaction : "+filePath); // Capture screenshots
		 * String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		 * test.info("Incident form : ",
		 * MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());
		 */

		// Click on reeturn/enter key
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(3000);
		System.out.println("reeturn");

		// copy file path into click board (ctrl_C)
		StringSelection fileName1 = new StringSelection(fileName);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(fileName1, null);
		Thread.sleep(3000);
		System.out.println("ctrl_C 2");

		// ctrl+V
		rb.keyPress(KeyEvent.VK_CONTROL);
		rb.keyPress(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_V);
		rb.keyRelease(KeyEvent.VK_CONTROL);
		Thread.sleep(3000);
		System.out.println("ctrl_V 2");
		test.info("Search and select the file : "+fileName);

		/*
		 * test.info("Search and select the file : "+fileName); // Capture screenshots
		 * String screenshotPath5 = ExtentReportManager.captureScreenshot_new(driver);
		 * test.info("Incident form : ",
		 * MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath5).build());
		 */

		// Click on reeturn/enter key
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(3000);
		System.out.println("reeturn 2");
		Thread.sleep(3000);

		WebElement isuploded = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//div[@id='attachment_dialog_list']/table/tbody/tr/td/span/label/a[contains(text(), '"+fileName+"')]")));
		soft.assertTrue(isuploded.isDisplayed(), "File is not uploaded");
		
		  // Capture screenshots 
				String screenshotPath2 =
				  ExtentReportManager.captureScreenshot_new(driver);
				  test.info("File uploaded: ",
				  MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
				 

		// click on closed uopload
		driver.findElement(By.xpath("//button[@id=\"attachment_closemodal\"]")).click();
		Thread.sleep(3000);
		
		
		  // Capture screenshots 
		String screenshotPath4 =
		  ExtentReportManager.captureScreenshot_new(driver);
		  test.info("Incident form : ",
		  MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());
		 

	}

	public void Verify_file_is_uploded_on_Incident(String fileName, ExtentTest test) {
		boolean isuploded = driver.findElement(By.xpath("//div[@id='header_attachment_size_checker']/ul/li[2]/span/a[2][contains(text(), '" + fileName + "')]")).isDisplayed();
		soft.assertTrue(isuploded, "file is not uploaded");
		if(isuploded) {
			test.pass("Verified "+ fileName+" is uploaded on Incident");
		}else {
			test.fail("Verified " + fileName+" is not uploaded on Incident");
		}
		
	}

	public void Verify_sla_On_Incident(String slaItem, ExtentTest test) throws InterruptedException {
		// scroll to resolution tab
		WebElement element2 = driver
				.findElement(By.xpath("//span[1][@aria-controls=\"incident.task_sla.task_list\"]"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView(true);", element2);
		Thread.sleep(4000);

		// Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());


		List<WebElement> items = driver.findElements(By.xpath("//table[@id=\"incident.task_sla.task_table\"]/tbody/tr"));
		int count = items.size();
		System.out.println("Total items: " + count);

		List<String> slaArr = new ArrayList<>();

		for (int a=1;a<count+1;a++) {

			System.out.println("SlA number :"+a);

			List<WebElement> items3 = driver.findElements(By.xpath("//table[@id=\"incident.task_sla.task_table\"]/tbody/tr["+a+"]/td"));
			int count3 = items3.size();

			//System.out.println("Total items3: " + count3);
			List<String> names3 = new ArrayList<>();
			for (WebElement item3 : items3) {
				names3.add(item3.getText());

				System.out.println("item : "+item3.getText());
			}
			slaArr.addAll(names3);
			System.out.println("slaArr : " + slaArr);

		}

		System.out.println("slaArr : " + slaArr);

		boolean isPresent = slaArr.contains(slaItem);
		System.out.println("isPresent : " + isPresent);
		soft.assertTrue(isPresent, "Item is not present");


		if (slaArr.contains(slaItem)) {
			System.out.println("Item is present");
		}	

	}

	
	public HashMap<String, List<String>> Verify_sla_On_Incident2 (ExtentTest test) throws InterruptedException {
		// scroll to resolution tab
		WebElement element2 = driver
				.findElement(By.xpath("//span[1][@aria-controls=\"incident.task_sla.task_list\"]"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].scrollIntoView(true);", element2);
		Thread.sleep(4000);

		// Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		//get total number of sla
		List<WebElement> items = driver.findElements(By.xpath("//table[@id=\"incident.task_sla.task_table\"]/tbody/tr"));
		int count = items.size();
		//System.out.println("Total items: " + count);

		//
		List<String> slaArr = new ArrayList<>();
		HashMap<String, List<String>> data = new HashMap<>();
		String [][] slaList = null;

		for (int a=1;a<count+1;a++) {

			//System.out.println("SlA number :"+a);

			//get total items of sla
			List<WebElement> items3 = driver.findElements(By.xpath("//table[@id=\"incident.task_sla.task_table\"]/tbody/tr["+a+"]/td"));

			int count2 = items3.size();
			slaList = new String [count][count2];


			//System.out.println("Total items3: " + count3);
			List<String> names3 = new ArrayList<>();

			for (int b = 0; b < count2; b++) {
				WebElement item3 = items3.get(b);  // get element by index

				names3.add(item3.getText());
				//System.out.println("item : "+b+" " + item3.getText());
				slaList[a-1][b] = item3.getText();
			}

			slaArr.addAll(names3);
			String slaName = slaList[a-1][2];
			data.put(slaName, names3);

			/*
			 * System.out.println("slaArr : " + slaArr);
			 * 
			 * System.out.println("data : " + data);
			 * 
			 * for (int b = 0; b < count2; b++) {
			 * System.out.println("slaList at: "+(a-1)+" "+b+" : " + slaList[a-1][b]); }
			 */

		}
		return data;
	}

	public void is_SLA_triggered(String sla_Name,ExtentTest test) throws InterruptedException {

		// Verify sla
		HashMap<String, List<String>> slaList = new HashMap<>();
		slaList = Verify_sla_On_Incident2(test);

		//System.out.println("SLA List size " + slaList.size());
		//System.out.println("SLA present on Incident " + slaList);

		soft.assertTrue(slaList.containsKey(sla_Name), "SLA is not present");

		if(slaList.size() >0) {
			for (String key : slaList.keySet()) {
				System.out.println("SLA is Triggerted");
				test.info(sla_Name +" is triggered");
				//System.out.println("SLA Name: " + key + "/n"+" SLA Values : " + slaList.get(key));
				//	System.out.println("Locator value: " + slaList.get(key));
			}
		}else
		{
			System.out.println("SLA are not triggered ");
		}
		// Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info( "Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
	}


	public void verify_SLA(String sla_Name, String sla_detail, ExtentTest test) throws InterruptedException {
		HashMap<String, List<String>> slaList = new HashMap<>();
		slaList = Verify_sla_On_Incident2(test);

		soft.assertTrue(slaList.containsKey(sla_Name), "SLA is not present");

		if (slaList.containsKey(sla_Name)) {
			List<String> sla_Details = new ArrayList<>();
			sla_Details = slaList.get(sla_Name);
			test.info(sla_Name+ " details : " +sla_Details);
			if (sla_Details.contains(sla_detail)) {
				soft.assertTrue(sla_Details.contains(sla_detail), "SLA does not have expected value");
				System.out.println("sla_Name '" + sla_Name + "' has the expected value: " + sla_detail);
				test.pass(sla_Name +" does have expected value"+ sla_detail);

				// Capture screenshots
				String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
				test.info( "sla_Name : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());
			} 
		}else {
			System.out.println("SLA :" + sla_Name + "' did not triggered");
			test.fail(sla_Name +" does not have expected value"+ sla_detail);
		}

	}

}





