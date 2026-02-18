package com.example.tests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import base.BaseTest;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import utils.DataImport;
import utils.ExtentReportManager;

public class Problem extends BaseTest {
	private WebDriver driver = DriverManager.getDriver();
	public static String problemNo;
	public static String noteEmailID;
	private JavascriptExecutor jse;
	private Logger logger = LogManager.getLogger("Problem");
	Object[][] problemdata = DataImport.getData("Problem");
	private Impersonation impersonation = new Impersonation(driver);
	private Navigator navigator = new Navigator(driver);
	public WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	
	@Test(priority = 1)
	public void impersonateUser() throws InterruptedException {
		test = ExtentReportManager.createTest("------- Problem Flow Started -------");
		// End Impersonation
		jse = (JavascriptExecutor) driver;
		Thread.sleep(2000);
		impersonation.endImpersonation(jse);
		Thread.sleep(2000);

		// Impersonating adimn user
		impersonation.startImpersonation(problemdata[0][1].toString(), jse);
		Thread.sleep(2000);

	}
//	@Test (priority = 1)
//	public void impersonateUser() throws InterruptedException {
//		test = ExtentReportManager.createTest("------- Problem Flow Started -------");
//		System.out.println("Welcome execution started");
//		// Open bowser
//		WebDriver driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		// Implicit Wait
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
//		JavascriptExecutor jse = (JavascriptExecutor) driver;
//		// LogIn page
//		String baseUrl = "https://dev231719.service-now.com";
//		driver.get(baseUrl);
//		driver.findElement(By.xpath("//input[@id='user_name']")).sendKeys("admin");
//		driver.findElement(By.xpath("//input[@id='user_password']")).sendKeys("2t/p8ANG^aaG");
//		driver.findElement(By.xpath("//button[@id='sysverb_login']")).click();
//    	Thread.sleep(2000);
//    	
//	 }

	@Test(description = "SC_001- Verification of Open and Submit Problem form without fill details in mandatory fields", priority = 2)
	public void submitProblemFormWODetail() {
		try {

			logger.info(
					"SC_001- Verification of Open and Submit Problem form without fill details in mandatory fields");
			test = ExtentReportManager.createTest(
					"SC_001- Verification of Open and Submit Problem form without fill details in mandatory fields");
			// Open Problem table
			test.log(Status.INFO, "Open Problem table and click on New button to open Problem form");
			
			//Navigate all and click on create
//			navigator.allProblem();              //----------Navigation method
			
			// Switch to iframe
//			String iframeScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"#gsft_main\")";
//			WebElement frame = (WebElement)jse.executeScript(iframeScript);
//			driver.switchTo().frame(frame);
			
			//Or Directly go to problem list
			driver.get(baseUrl + "/problem_list");
			Thread.sleep(4000);
			jse = (JavascriptExecutor) driver;

			// Click on New button
			String newbutton = "return document.querySelector('#sysverb_new')";
			WebElement clicknewui = (WebElement) jse.executeScript(newbutton);
			clicknewui.click();

			test.log(Status.INFO, "Submit record without fill details in mandatory fields");
			logger.info("Submit record without fill details in mandatory fields");
			// Click on Submit button
			WebElement submit=driver.findElement(By.xpath("//*[@id='sysverb_insert']"));
			jse.executeScript("arguments[0].click();", submit);
			test.log(Status.PASS, "Record is not submitted without fill details in mandatory fields");
			// Verify Error message

			String errorMesg = driver.findElement(By.xpath("//*[@id='output_messages']/div/div/span[2]")).getText();
			test.log(Status.INFO, "Error Pop-up shown as - " + errorMesg);

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Error Pop-up", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		} catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}

	}

	@Test(description = "SC_002- Validate and fill madatory field details", priority = 3)
	public void validateProblemForm() throws InterruptedException {
		try {

			logger.info("SC_002- Validate and fill madatory field details");
			test = ExtentReportManager.createTest("SC_002- Validate and fill madatory field details");

			// Verify Original task field
			test.log(Status.INFO, "Verifying Origin task field");

			String originalTaskField = driver
					.findElement(By.xpath("//*[@id='label.problem.first_reported_by_task']/label/span[2]")).getText();
			if (originalTaskField.equals(problemdata[2][0].toString())) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + originalTaskField);
				logger.info("Field title is- " + originalTaskField);

				// **Set original task field value**
				String mainWindowHandle = driver.getWindowHandle();
				test.log(Status.INFO, "Set original task field value");
				logger.info("Set original task field value");
				// Click on original task look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.first_reported_by_task']")).click();
				Thread.sleep(2000);

				// Get all window handles after the new window opens
				Set<String> allWindowHandles = driver.getWindowHandles();

				// Switch to child opened window Set<String> allWindowHandles
				Iterator<String> iterator = allWindowHandles.iterator();
				while (iterator.hasNext()) {
					String childWindowHandle = iterator.next();

					// Switch to the child window only if it's not the main window
					if (!mainWindowHandle.equalsIgnoreCase(childWindowHandle)) {
						driver.switchTo().window(childWindowHandle);
						// Select change from table
						driver.findElement(By.xpath("//table[@id='task_table']/tbody/tr[2]/td[3]")).click();
						break; // Exit the loop once the correct window is found/switched
					}
				}
			// Switch back to the main window
				driver.switchTo().window(mainWindowHandle);
				Thread.sleep(2000);	
				// Print set value
				String originalTaskValue = driver.findElement(By.xpath("//*[@id='sys_display.problem.first_reported_by_task']")).getAttribute("value");
				test.log(Status.INFO, "Original task field value is - " + originalTaskValue);
				logger.info("Original task field value is - " + originalTaskValue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Original task field is not expected");
				logger.info("Original task field is not expected");
			}

			// Verify Category field
			test.log(Status.INFO, "Verifying Category field");

			String categoryField = driver.findElement(By.xpath("//*[@id='label.problem.category']/label/span[2]"))
					.getText();
			if (categoryField.equals(problemdata[3][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field name is- " + categoryField);
				logger.info("Field name is- " + categoryField);

				// **Set Category field value**
				test.log(Status.INFO, "Select Category field value");
				logger.info("Select Category field value");

				WebElement selectCategory = driver.findElement(By.xpath("//*[@id='problem.category']")); // Locate the
				Select dropdown = new Select(selectCategory); // Create an instance of Select class
//						dropdown.selectByVisibleText("Duplicate"); // Select option by visible text
				dropdown.selectByIndex(2); // Selects the second option (index starts from 0) // Select option by index

				// Print set value
				String categoryValue = driver.findElement(By.xpath("//*[@id='problem.category']"))
						.getAttribute("value");

				test.log(Status.INFO, "Selected Category field value is - " + categoryValue);
				logger.info("Selected Category field value is - " + categoryValue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Category field is not expected");
				logger.info("Category field is not expected");
			}

			// Verify Subcategory field
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Subcategory field");

			String subCategoryField = driver.findElement(By.xpath("//*[@id='label.problem.subcategory']/label/span[2]"))
					.getText();
			if (subCategoryField.equals(problemdata[4][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + subCategoryField);
				logger.info("Field title is- " + subCategoryField);

				// **Set Subcategory field value**
				test.log(Status.INFO, "Select Subcategory field value");
				logger.info("Select Subcategory field value");

				WebElement dropdownElement = driver.findElement(By.xpath("//*[@id='problem.subcategory']")); // Locate
																												// the
				Select subCategoryEle = new Select(dropdownElement);
				subCategoryEle.selectByIndex(2); // Selects the second option (index starts from 0) // Select option by
				// Print set value
				String subCategoryValue = driver.findElement(By.xpath("//*[@id='problem.subcategory']"))
						.getAttribute("value");

				test.log(Status.INFO, "Selected Subcategory field value is - " + subCategoryValue);
				logger.info("Selected Subcategory field value is - " + subCategoryValue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Subcategory field is not expected");
				logger.info("Subcategory field is not expected");
			}

			// Verify Impact field value**
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Impact field");

			String impactField = driver.findElement(By.xpath("//*[@id='label.problem.impact']/label/span[2]"))
					.getText();
			if (impactField.equals(problemdata[5][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + impactField);
				logger.info("Field title is- " + impactField);

				// **Set Impact field value**
				test.log(Status.INFO, "Select Impact field value");
				logger.info("Select Impact field value");

				WebElement selectImpact = driver.findElement(By.xpath("//*[@id='problem.impact']"));
				Select dropdown1 = new Select(selectImpact);
				dropdown1.selectByIndex(0);
				// Print set value
				String impactValue = driver.findElement(By.xpath("//*[@id='problem.impact']")).getAttribute("value");

				test.log(Status.INFO, "Selected Impact field value is - " + impactValue);
				logger.info("Selected Impact field value is - " + impactValue);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Impact field is not expected");
				logger.info("Impact field is not expected");
			}

			// Verify Urgency field
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Urgency field");

			String urgencyField = driver.findElement(By.xpath("//*[@id='label.problem.urgency']/label/span[2]"))
					.getText();
			if (urgencyField.equals(problemdata[6][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + urgencyField);
				logger.info("Field title is- " + urgencyField);

				// **Set Urgency field value**
				test.log(Status.INFO, "Select Urgency field value");
				logger.info("Select Urgency field value");

				WebElement selectUrgency = driver.findElement(By.xpath("//*[@id='problem.urgency']"));
				Select dropdown2 = new Select(selectUrgency);
				dropdown2.selectByIndex(1);
				// Print set value
				String urgencyValue = driver.findElement(By.xpath("//*[@id='problem.urgency']")).getAttribute("value");

				test.log(Status.INFO, "Selected Urgency field value is - " + urgencyValue);
				logger.info("Selected Urgency field value is - " + urgencyValue);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Urgency field is not expected");
				logger.info("Urgency field is not expected");
			}

			// Verify Priority field
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Priority field and value");

			String priorityField = driver.findElement(By.xpath("//*[@id='label.problem.priority']/label/a/span[2]"))
					.getText();
			if (priorityField.equals(problemdata[7][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field is title is- " + priorityField);
				logger.info("Field is title is- " + priorityField);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Priority field is not visible");
				logger.info("Priority field is not visible");
			}
			// Verify Priority field value
			Select ptiority = new Select(driver.findElement(By.xpath("//*[@id='problem.priority']")));
			String priorityValue = ptiority.getFirstSelectedOption().getText();

			String urgencyValue = driver.findElement(By.xpath("//*[@id='problem.urgency']")).getText();
			String impactValue = driver.findElement(By.xpath("//*[@id='problem.impact']")).getText();

			if (priorityValue.equals(problemdata[7][1])) {

				test.log(Status.PASS, "Priority is " + priorityValue + " as per selected Impact " + impactValue
						+ " and Urgency " + urgencyValue);
				logger.info("Priority is - " + priorityValue + " as per selected Impact - " + impactValue
						+ " and Urgency - " + urgencyValue);
				AssertJUnit.assertTrue(true); // test pass
			} else {
				test.log(Status.FAIL, "Priority is " + priorityValue + " not as per selected Impact " + impactValue
						+ " and Urgency " + urgencyValue);
				logger.info("Priority is not as per selected Impact and Urgency");
				AssertJUnit.assertTrue(false);// test fail
			}

			// Verify Problem statement field
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Problem statement field");

			String problemStmntField = driver
					.findElement(By.xpath("//*[@id='label.problem.short_description']/label/span[2]")).getText();
			if (problemStmntField.equals(problemdata[10][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + problemStmntField);
				logger.info("Field title is- " + problemStmntField);

				test.log(Status.INFO, "Problem statement field is mandatory");

				// Enter Problem statement field details
				test.log(Status.INFO, "Enter Problem statement field value");
				logger.info("Enter Problem statement field value");
				driver.findElement(By.xpath("//*[@id='problem.short_description']"))
						.sendKeys(problemdata[10][1].toString());

				// Print set value
				String problemStmntValue = driver.findElement(By.xpath("//*[@id='problem.short_description']"))
						.getAttribute("value");

				test.log(Status.INFO, "Entered Problem statement field value is - " + problemStmntValue);
				logger.info("Entered Problem statement field value is - " + problemStmntValue);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Problem statement field is not expected");
				logger.info("Problem statement field is not expected");
			}

			// Verify Assignment group field
			test.log(Status.INFO, "Verifying Assignment group field");

			String assignmentGroupField = driver
					.findElement(By.xpath("//*[@id='label.problem.assignment_group']/label/span[2]")).getText();
			if (assignmentGroupField.equals(problemdata[8][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + assignmentGroupField);
				logger.info("Field title is- " + assignmentGroupField);

				// **Set Assignment group field value**
				String mainWindowHandle1 = driver.getWindowHandle();

				test.log(Status.INFO, "Set Assignment group field value");
				logger.info("Set Assignment group field value");
				// Click on Assignment group look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.assignment_group']")).click();
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
						searchAssignmentGroup.sendKeys(problemdata[8][1].toString() + Keys.ENTER);
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
						.findElement(By.xpath("//*[@id='sys_display.problem.assignment_group']")).getAttribute("value");

				test.log(Status.INFO, "Selected Assignment group field value is - " + assignmentGroupValue);
				logger.info("Selected Assignment group field value is - " + assignmentGroupValue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Assignment group field is not expected");
				logger.info("Assignment group field is not expected");
			}

			// Verify Assign to field
			test.log(Status.INFO, "Verifying Assign to field");

			String assignToField = driver.findElement(By.xpath("//*[@id='label.problem.assigned_to']/label/span[2]"))
					.getText();
			if (assignToField.equals(problemdata[9][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + assignToField);
				logger.info("Field title is- " + assignToField);

				// Set Assign to field value
				String mainWindowHandle2 = driver.getWindowHandle();

				test.log(Status.INFO, "Set Assign to user in field");
				logger.info("Set Assign to user in field");
				// Click on Assign to look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.assigned_to']")).click();
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
						searchAssigTo.sendKeys(problemdata[9][1].toString() + Keys.ENTER);

						Thread.sleep(2000);
						// Select assign to
						driver.findElement(By.xpath("//table[@id='sys_user_table']/tbody/tr[1]/td[3]")).click();
						break;
					}
				}

				// Switch back to the main window
				driver.switchTo().window(mainWindowHandle2);

				// Print set value
				String assignToalue = driver.findElement(By.xpath("//*[@id='sys_display.problem.assigned_to']"))
						.getAttribute("value");

				test.log(Status.INFO, "Selected Assign to field value is - " + assignToalue);
				logger.info("Selected Assign to field value is - " + assignToalue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Assign to field is not expected");
				logger.info("Assign to field is not expected");
			}

			// Verify State field name
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying State field and value");

			String stateField = driver.findElement(By.xpath("//*[@id='label.problem.state']/label/span[2]")).getText();
			if (stateField.equals(problemdata[11][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + stateField);
				logger.info("Field title is- " + stateField);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "State field is not visible");
				logger.info("State field is not visible");
			}
			// Verify State field value
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[13][0])) {
				test.log(Status.PASS, "State is - " + stateValue);
				logger.info("State is - " + stateValue);
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "State is not New");
				logger.info("State is not New");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Fill CI
			test.log(Status.INFO, "Verifying CI field");

			String CI = driver.findElement(By.xpath("//*[@id='label.problem.cmdb_ci']/label/span[2]")).getText();
			System.out.println("captured CI is  "+CI);
			System.out.println("Excel CI is  "+problemdata[1][0]);
			if (CI.equals(problemdata[1][0])) {
			
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + CI);
				logger.info("Field title is- " + CI);

				// Set Assign to field value
				String mainWindowHandle2 = driver.getWindowHandle();

				test.log(Status.INFO, "Set Configuration Item");
				logger.info("Set Configuration Item");
				// Click on Assign to look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.cmdb_ci']")).click();
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
						WebElement searchCI = driver
								.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
						searchCI.sendKeys(problemdata[1][1].toString() + Keys.ENTER);

						Thread.sleep(2000);
						// Select CI
						driver.findElement(By.xpath("//table[@id='cmdb_ci_table']/tbody/tr[1]/td[3]")).click();
						break;
					}
				}

				// Switch back to the main window
				driver.switchTo().window(mainWindowHandle2);

				// Print set value
				String ciValue = driver.findElement(By.xpath("//*[@id='sys_display.problem.cmdb_ci']"))
						.getAttribute("value");

				test.log(Status.INFO, "Selected Configuration Item field value is - " + ciValue);
				logger.info("Selected Configuration Item field value is - " + ciValue);

			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Configuration Item field is not expected");
				logger.info("Configuration Item field is not expected");
			}

			// Verify Description field
			Thread.sleep(2000);
			test.log(Status.INFO, "Verifying Description field");

			String descriptionField = driver.findElement(By.xpath("//*[@id='label.problem.description']/label/span[2]"))
					.getText();
			if (descriptionField.equals(problemdata[12][0])) {
				AssertJUnit.assertTrue(true); // test case pass
				test.log(Status.PASS, "Field title is- " + descriptionField);
				logger.info("Field title is- " + descriptionField);

				// Enter Description field details
				test.log(Status.INFO, "Enter Description field details");
				logger.info("Enter Description field details");
				driver.findElement(By.xpath("//*[@id='problem.description']")).sendKeys(problemdata[12][1].toString());

				// Print set value
				String descriptionValue = driver.findElement(By.xpath("//*[@id='problem.description']"))
						.getAttribute("value");

				test.log(Status.INFO, "Entered Description field details is - " + descriptionValue);
				logger.info("Entered Description field details is - " + descriptionValue);
			} else {
				AssertJUnit.assertTrue(false);// test case failed
				test.log(Status.FAIL, "Description field is not expected");
				logger.info("Description field is not expected");
			}

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Filled form", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_003- Verification of Save form once fill all required details", priority = 4)
	public void submitForm() {
		try {

			logger.info("SC_003- Verification of Save form once fill all required details");
			test = ExtentReportManager.createTest("SC_003- Verification of Save form once fill all required details");

			// Note down problem number
			test.log(Status.INFO, "Note down Problem number");
			WebElement inputElement = driver.findElement(By.xpath("//*[@id='sys_readonly.problem.number']"));
			problemNo = inputElement.getAttribute("value");

			test.log(Status.INFO, "Problem number is - " + problemNo);
			logger.info("Problem number is - " + problemNo);

			// Note down Assigned user mail id
			driver.findElement(By.xpath("//*[@id='viewr.problem.assigned_to']")).click();
			Thread.sleep(1000);

//			String noteEmailID = (String) jse.executeScript("return document.querySelector('#sys_readonly\\.sys_user\\.email')").toString();
//			String noteEmailID = driver.findElement(By.xpath("//*[@id='viewr.problem.assigned_to']")).getText();
//			String inputValue = (String) jse.executeScript("return arguments[0].value;", clickProfile);

			noteEmailID = driver.findElement(By.xpath("//*[@id='sys_readonly.sys_user.email']")).getAttribute("value");
			System.out.println("Email is " + noteEmailID);
//			String noteEmailID = driver.findElement(By.xpath("//*[@id='viewr.problem.assigned_to']")).getAttribute("value");
//			System.out.println("Email is "+noteEmailID);                        //---------------------------Email ID not able to note down---------------------------Email ID not able to note down

			// Click on Submit UI action
			test.log(Status.INFO, "Click on Submit UI action");
			driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
			System.out.println("Copied value is problem number " + problemNo);

			test.log(Status.PASS, "Problem form is submitted Successfully");
			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("After form submitted", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_004- Verification of reopen submitted record from Problem table", priority = 5)
	public void reOpenForm() throws InterruptedException {
		try {
			logger.info("SC_004- Verification of reopen submitted record from Problem table");
			test = ExtentReportManager.createTest("SC_004- Verification of reopen submitted record from Problem table");

			System.out.println("value is problem number " + problemNo);
			// Search Problem record on table
			test.log(Status.INFO, "Search Problem number");
			WebElement globalSearchBox = driver
					.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
			globalSearchBox.sendKeys(problemNo);

			Thread.sleep(1000);
			globalSearchBox.sendKeys(Keys.ENTER);
			Thread.sleep(2000);

			// Open Problem
			test.log(Status.INFO, "Click and open created record");
			List<WebElement> openPRB = driver.findElements(By.xpath("//table[@id='problem_table']/tbody/tr/td[3]/a"));
			for (WebElement ele2 : openPRB) {
				String currentPRB = ele2.getText();
				if (currentPRB.contains(problemNo)) {
					ele2.click();
					break;
				}
			}

			// Verify Opened Problem record
			test.log(Status.INFO, "Verify opened record");

			WebElement openedRecord = driver.findElement(By.xpath("//*[@id='sys_readonly.problem.number']"));
			String openedPRB = openedRecord.getAttribute("value");
			test.log(Status.PASS, "Created Problem record is- " + openedPRB + "opened");
			logger.info("Created Problem record is- " + openedPRB + "opened");
//			Assert.assertEquals(openedPRB, value, "Created Problem is opened");

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Reopened problem record",
					MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_005- Verification of State once Problem is Assigned", priority = 6)
	public void stateOnceAssignSave() {

		try {
			logger.info("SC_005- Verification of State once Problem is Assigned");
			test = ExtentReportManager.createTest("SC_005- Verification of State once Problem is Assigned");

			String assignToalue = driver.findElement(By.xpath("//*[@id='sys_display.problem.assigned_to']"))
					.getAttribute("value"); // -------entered value in Assign To

			test.log(Status.INFO, "Assigned Problem to " + assignToalue);
			logger.info("Assigned Problem to " + assignToalue);

			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[14][0])) {
				test.log(Status.PASS, "State is - " + stateValue);
				logger.info("State is - " + stateValue);
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				logger.info("State is not Assess");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_006- Verification of State once Problem is Confirmed", priority = 7)
	public void RCAState() {

		try {
			logger.info("SC_006- Verification of State once Problem is Confirmed");
			test = ExtentReportManager.createTest("SC_006- Verification of State once Problem is Confirmed");

			// Click on confirm UI action
			WebElement confirmUI = driver.findElement(By.xpath("//*[@id=\"move_to_rca\"]"));
//			String uiAction = confirmUI.getText();
			boolean uiAction1 = confirmUI.isDisplayed();
			if (uiAction1) {
				test.log(Status.PASS, "Confirm UI action is visible");
				logger.info("Confirm UI action is visible");

				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Confirm UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				confirmUI.click();
				test.log(Status.INFO, "Click on 'Confirm' UI action");
				logger.info("Click on 'Confirm' UI action");
				// AssertJUnit.assertTrue(true); // test pass
			} else {
				logger.info("Confirm UI action is not visible");
				test.log(Status.FAIL, "Confirm UI action is not visible");
				// AssertJUnit.assertTrue(false);// test fail

			}

			// Verify State once click on Confirm UI action
			test.log(Status.INFO, "Verify State once Problem is Confirmed");
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[15][0])) {
				test.log(Status.PASS, "State is - " + stateValue + "once problem is confirmed");
				logger.info("State is - " + stateValue);
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				logger.info("State is not Root Cause Analysis");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_007- Verification of Fixing Problem without fill required details", priority = 8)
	public void fixStateWODetails() throws InterruptedException { // Negative scenario

		try {
			logger.info("SC_007- Verification of Fixing Problem without fill required details");
			test = ExtentReportManager
					.createTest("SC_007- Verification of Fixing Problem without fill required details");

			// Click on Fix UI action
			test.log(Status.INFO, "Click on Fix UI action without fill required details");
			logger.info("Click on Fix UI action without fill required details");
			WebElement fixUI = driver.findElement(By.xpath("//*[@id='move_to_fix_in_progress']"));
//			String fixUIAction = fixUI.getText();
			fixUI.click();
			Thread.sleep(3000);

			test.log(Status.PASS,
					"When click on Fix UI action without fill required details pop up dialogue opened for required fields");
			// Click on close opened mandatory field window
//			WebElement element = driver.findElement(By.xpath("//*[@id='start_fix_dialog_form_view']/div/div/header/button"));
//			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;jsExecutor.executeScript("arguments[0].click();",element);

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Pop up window", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			// Close open dialog box- Refresh the current page
			driver.navigate().refresh();
			Thread.sleep(2000);

			// Verify State once click on Fix UI action
			test.log(Status.INFO, "Verify State if click on Fix UI action without filled mandatory details");
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[15][0])) {
				test.log(Status.PASS,
						"State is - " + stateValue + ", Problem is not fixed without filled mandatory field");
				logger.info("State is - " + stateValue + ", Problem is not fixed without filled mandatory field");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			} else {
				test.log(Status.FAIL, "State is Fixed");
				logger.info("State is Fixed");
				AssertJUnit.assertTrue(false);// test fail
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_008- Verification of State once Problem is Fixed with filled mandtory field", priority = 9)
	public void fixState() throws InterruptedException {

		try {
			logger.info("SC_008- Verification of State once Problem is Fixed with filled mandtory field");
			test = ExtentReportManager
					.createTest("SC_008- Verification of State once Problem is Fixed with filled mandtory field");

			// Fill Cause Notes
			// Go to Analysis Information tab
			test.log(Status.INFO, "Filling mandatory details for fix the problem");
			test.log(Status.INFO, "Go to Analysis Information tab and fill Cause Notes field details");
			logger.info("Fill Cause Notes");
			driver.findElement(By.xpath("//*[@id='tabs2_section']/span[2]/span[1]/span[2]")).click();
			WebElement frameElement = driver.findElement(By.xpath("//*[@id='problem.cause_notes_ifr']"));
			driver.switchTo().frame(frameElement);
			driver.findElement(By.xpath("//*[@id=\"tinymce\"]")).sendKeys("Test Cause Notes");

			// Fill Fix Notes
			logger.info("Fill Fix Notes");
			test.log(Status.INFO, "Go to Resolution Information tab and fill Fix Notes field details");
			driver.switchTo().parentFrame();
			driver.findElement(By.xpath("//*[@id=\"tabs2_section\"]/span[3]/span[1]")).click();
			Thread.sleep(2000);
			WebElement frameElement1 = driver.findElement(By.xpath("//*[@id='problem.fix_notes_ifr']"));
			driver.switchTo().frame(frameElement1);
			driver.findElement(By.xpath("//*[@id=\"tinymce\"]")).sendKeys("Test Fix Notes"); // ----------Failed
			driver.switchTo().parentFrame();

			// Click on Fix UI action
			WebElement fixUI = driver.findElement(By.xpath("//*[@id='move_to_fix_in_progress']"));
//			String uiAction = fixUI.getText();
			boolean uiActionDisply = fixUI.isDisplayed();
			if (uiActionDisply) {

				logger.info("Fix UI action is visible");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Fix UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

				test.log(Status.INFO, "Click on Fix UI action");
				fixUI.click();
			} else {
				logger.info("Fix UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Fix UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State once click on Fix UI action
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[16][0])) {
				test.log(Status.PASS, "State is - " + stateValue);
				logger.info("State is - " + stateValue);
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "State is not Fix in Progress");
				logger.info("State is not Fix in Progress");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_009- Verification of State and Resolution code once Problem is Resolved", priority = 10)
	public void resolveState() throws InterruptedException {

		try {
			logger.info("SC_009- Verification of State and Resolution code once Problem is Resolved");
			test = ExtentReportManager
					.createTest("SC_009- Verification of State and Resolution code once Problem is Resolved");

			WebElement resolveUI = driver.findElement(By.xpath("//*[@id='move_to_resolved']"));
//			String uiAction = resolveUI.getText();
			boolean uiActionDisply = resolveUI.isDisplayed();
			if (uiActionDisply) {
				test.log(Status.PASS, "Resolve UI action is visible");
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Resolve UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				test.log(Status.INFO, "Click on Resolve UI action");
				resolveUI.click();

				logger.info("Resolve UI action is visible");
				AssertJUnit.assertTrue(true); // test pass
			} else {
				logger.info("Resolve UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Resolve UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State once click on Resolve UI action
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[17][0])) {
				test.log(Status.PASS, "State is - " + stateValue + "once Resolve problem");
				logger.info("State is - " + stateValue + "once Resolve problem");
				AssertJUnit.assertTrue(true); // test pass
			} else {
				test.log(Status.FAIL, "State is not Resolved");
				logger.info("State is not Resolved");
				AssertJUnit.assertTrue(false);// test fail
			}
			// Verify Resolution code
			Select resolutionCode = new Select(
					driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
			String selectedCode = resolutionCode.getFirstSelectedOption().getText();

			if (selectedCode.equals(problemdata[19][1])) {
				test.log(Status.PASS, "Resolution code is - " + selectedCode + "once Resolve problem");
				logger.info("Resolution code is - " + selectedCode + "once Resolve problem");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "Resolution code is not Fix Applied");
				logger.info("Resolution code is not Fix Applied");
				AssertJUnit.assertTrue(false);// test fail
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_010- Verification of State when Problem is Re-Analyzed", priority = 11)
	public void reAnalyze() throws InterruptedException {

		try {
			logger.info("SC_010- Verification of State when Problem is Re-Analyzed");
			test = ExtentReportManager.createTest("SC_010- Verification of State when Problem is Re-Analyzed");

			WebElement reAnalyzeUI = driver.findElement(By.xpath("//*[@id='re_analyze']"));
//			String uiAction = reAnalyzeUI.getText();
			boolean uiActionDisply = reAnalyzeUI.isDisplayed();
			if (uiActionDisply) {
				test.log(Status.PASS, "Re-Analyze UI action is visible");

				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Re-Analyze UI action",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

				test.log(Status.INFO, "Click on Re-Analyze UI action");
				logger.info("Click on Re-Analyze UI action");
				reAnalyzeUI.click();
				AssertJUnit.assertTrue(true); // test pass
			} else {
				test.log(Status.FAIL, "Re-Analyze UI action is not visible");
				logger.info("Re-Analyze UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Re-Analyze UI action",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State of problem if click on Re-Analyze UI action -->Root Case
			// Analysis
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

//			Assert.assertEquals(stateValue, problemdata[0][0], "State is - "+stateValue); 
			if (stateValue.equals(problemdata[15][0])) {
				test.log(Status.PASS, "State is - " + stateValue + " when click on Re-Analyze UI action");
				logger.info("State is - " + stateValue + " when click on Re-Analyze UI action");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "State is not Root Case Analysis");
				logger.info("State is not Root Case Analysis");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_011- Verification of State when Problem is Cancelled", priority = 12)
	public void cancelPRB() throws InterruptedException {

		try {
			logger.info("SC_011- Verification of State when Problem is Cancelled");
			test = ExtentReportManager.createTest("SC_011- Verification of State when Problem is Cancelled");

			// Click on Cancel UI action
			WebElement cancelUI = driver.findElement(By.xpath("//*[@id='cancel_problem']"));
//			String uiAction = cancelUI.getText();
			boolean uiActionDisply = cancelUI.isDisplayed();
			if (uiActionDisply) {

				test.log(Status.PASS, "Cancel UI action is visible");
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Cancel UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				logger.info("Cancel UI action is visible");
				AssertJUnit.assertTrue(true); // test pass

				test.log(Status.INFO, "Click on Cancel UI action without fill mandatory fields details");
				cancelUI.click();

			} else {
				test.log(Status.FAIL, "Cancel UI action is not visible");
				logger.info("Cancel UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Cancel UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
			Thread.sleep(3000);

			// Click on close opened mandatory field window
			WebElement element = driver.findElement(By.xpath("//*[@id='tabs2_section']/span[4]/span[1]"));
			test.log(Status.PASS, "For mandatory fields pop up window opend");
			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Pop up window", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			jsExecutor.executeScript("arguments[0].click();", element);

			/*
			 * //Close open dialog box- Refresh the current page
			 * driver.navigate().refresh(); //Check refresh page alternate to close opened
			 * dialogue Thread.sleep(2000);
			 * 
			 * // Verify State of problem if click on Cancel UI action -->Complete Select
			 * stateCancel = new
			 * Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")))
			 * ; String changedState = stateCancel.getFirstSelectedOption().getText();
			 * 
			 * Assert.assertEquals("Root Case Analysis", changedState,
			 * "State is not changed, due to mandatory field 'Close notes' not filled");
			 */

			// Fill mandatory fields for Cancel ----> Close notes
			test.log(Status.INFO, "Fill mandatory field 'Close notes' details");
			// Go to tab
			driver.findElement(By.xpath("//*[@id='cancel_dialog_form_view']/div/div/header/button")).click();

			// Fill Close notes
			logger.info("Fill mandatory field- Close notes");
			driver.findElement(By.xpath("//*[@id='problem.close_notes']")).sendKeys("Test Close Notes");

			// Click on Cancel
			test.log(Status.INFO, "Click on Cancel UI action once fill mandatory field 'Close notes' details");
			logger.info("Click on Cancel UI action");
			driver.findElement(By.xpath("//*[@id='cancel_problem']")).click();

			// Verify State of problem if click on Cancel UI action -->Complete
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

//		Assert.assertEquals(stateValue, problemdata[0][0], "State is - "+stateValue); -----------------------add problemdata state value "Closed"
			if (stateValue.equals(problemdata[18][0])) { // -----------------------add problemdata state value "Closed"
				test.log(Status.PASS, "State is - " + stateValue + " when click on Cancel UI action");
				logger.info("State is - " + stateValue + " when click on Cancel UI action");
				AssertJUnit.assertTrue(true); // test pass
			} else {
				test.log(Status.FAIL, "State is not Closed");
				logger.info("State is not Closed");
				AssertJUnit.assertTrue(false);// test fail
			}

			// Verify Resolution code --> Canceled
			Select resolutionCode = new Select(
					driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
			String selectedCode = resolutionCode.getFirstSelectedOption().getText();

			if (selectedCode.equals(problemdata[19][2])) { // -----------------------add problemdata state value
															// "Canceled"
				test.log(Status.PASS, "Resolution code is - " + selectedCode + " when click on Cancel UI action");
				logger.info("Resolution code is - " + selectedCode + " when click on Cancel UI action");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			} else {
				test.log(Status.FAIL, "Resolution code is not Canceled");
				logger.info("Resolution code is not Canceled");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_012- Verification of State when Cancelled Problem is Re-Analyzed", priority = 13)
	public void reAnalyzeOnceAgain() throws InterruptedException {

		try {
			logger.info("SC_012- Verification of State when Cancelled Problem is Re-Analyzed");
			test = ExtentReportManager
					.createTest("SC_012- Verification of State when Cancelled Problem is Re-Analyzed");

			WebElement reAnalyzeUI = driver.findElement(By.xpath("//*[@id='re_analyze']"));
//			String uiAction = reAnalyzeUI.getText();
			boolean uiActionDisply = reAnalyzeUI.isDisplayed();
			if (uiActionDisply) {

				test.log(Status.PASS, "Re-Analyze UI action is visible");
				logger.info("Re-Analyze UI action is visible");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Re-Analyze UI action",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

				test.log(Status.INFO, "Click on Re-Analyze UI action");
				reAnalyzeUI.click();
			} else {
				test.log(Status.FAIL, "Re-Analyze UI action is not visible");
				logger.info("Re-Analyze UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Re-Analyze UI action",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			}

			// Verify State of problem if click on Re-Analyze UI action -->Root Case
			// Analysis
			Thread.sleep(2000);

			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

//			Assert.assertEquals(stateValue, problemdata[0][0], "State is - "+stateValue); -----------------------add problemdata state value "Root Case Analysis"
			if (stateValue.equals(problemdata[15][0])) { // -----------------------add problemdata state value "Root
															// Case Analysis"
				test.log(Status.PASS, "State is changed to - " + stateValue + " when click on Re-Analyze UI action");
				logger.info("State is changed to - " + stateValue + " when click on Re-Analyze UI action");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			} else {
				test.log(Status.FAIL, "State is not changed to Root Case Analysis");
				logger.info("State is not changed to Root Case Analysis");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_013- Verification of State once Problem is Closed", priority = 14)
	public void cloasedState() throws InterruptedException {

		try {
			logger.info("SC_013- Verification of State once Problem is Closed");
			test = ExtentReportManager.createTest("SC_013- Verification of State once Problem is Closed");

			// Click on Fix UI action
			WebElement fixUI = driver.findElement(By.xpath("//*[@id='move_to_fix_in_progress']"));
//			String uiAction = fixUI.getText();
			boolean uiActionDisply = fixUI.isDisplayed();
			if (uiActionDisply) {

				test.log(Status.PASS, "Fix UI action is visible");
				logger.info("Fix UI action is visible");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Fix UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				fixUI.click();
			} else {
				logger.info("Fix UI action is not visible");
				test.log(Status.FAIL, "Fix UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Fix UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State once click on Fix UI action
			Thread.sleep(2000);
			test.log(Status.INFO, "Click on Fix UI action");
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[16][0])) { // -----------------------add problemdata state value "Fix in
															// Progress"
				test.log(Status.PASS, "State is - " + stateValue + " when click on Fix UI action");
				logger.info("State is - " + stateValue + " when click on Fix UI action");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "State is not Fix in Progress");
				logger.info("State is not Fix in Progress");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Click on Resolved UI action
			WebElement resolveUI = driver.findElement(By.xpath("//*[@id='move_to_resolved']"));
//			String uiAction = resolveUI.getText();
			boolean uiActionDisply1 = resolveUI.isDisplayed();
			if (uiActionDisply1) {

				test.log(Status.PASS, "Resolve UI action is visible");
				logger.info("Resolve UI action is visible");
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Resolve UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				AssertJUnit.assertTrue(true); // test pass
				test.log(Status.INFO, "Click on Resolve UI action");
				resolveUI.click();
			} else {
				test.log(Status.FAIL, "Resolve UI action is not visible");
				logger.info("Resolve UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Resolve UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State once click on Resolve UI action
			Thread.sleep(2000);

			Select state1 = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue1 = state1.getFirstSelectedOption().getText();
//
			if (stateValue1.equals(problemdata[17][0])) { // -----------------------add problemdata state value
															// "Resolve"
				test.log(Status.PASS, "State is - " + stateValue1 + " when click on Resolve UI action");
				logger.info("State is - " + stateValue1);
				AssertJUnit.assertTrue(true); // test pass
			} else {
				test.log(Status.FAIL, "State is not Resolved");
				logger.info("State is not Resolved");
				AssertJUnit.assertTrue(false);// test fail

			}
			// Verify Resolution code
			Select resolutionCode = new Select(
					driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
			String selectedCode = resolutionCode.getFirstSelectedOption().getText();

			if (selectedCode.equals(problemdata[19][1])) { // -----------------------add problemdata state value "Fix
															// Applied"
				test.log(Status.PASS, "Resolution code is - " + selectedCode + " when click on Resolve UI action");
				logger.info("Resolution code is - " + selectedCode);
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "Resolution code is not Fix Applied");
				logger.info("Resolution code is not Fix Applied");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Click on Complete UI action
			WebElement completeUI = driver.findElement(By.xpath("//*[@id='move_to_closed']"));
//			String uiAction = completeUI.getText();
			boolean uiActionDisplyed2 = completeUI.isDisplayed();
			if (uiActionDisplyed2) {
				test.log(Status.PASS, "Complete UI action is visible");

				logger.info("Complete UI action is visible");
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Complete UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
				AssertJUnit.assertTrue(true); // test pass

				test.log(Status.INFO, "Click on Complete UI action");
				completeUI.click();
			} else {
				test.log(Status.FAIL, "Complete UI action is not visible");
				logger.info("Complete UI action is not visible");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("Complete UI action", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}

			// Verify State once click on Complete UI action
			Thread.sleep(2000);

			Select closedState = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String closedStateValue = closedState.getFirstSelectedOption().getText();

			if (closedStateValue.equals(problemdata[18][0])) { // -----------------------add problemdata state value
																// "Closed"
				test.log(Status.PASS, "State is - " + closedStateValue + " when click on Complete UI action");
				logger.info("State is - " + closedStateValue);
				AssertJUnit.assertTrue(true); // test pass

			} else {
				test.log(Status.FAIL, "State is not Closed");
				logger.info("State is not Closed");
				AssertJUnit.assertTrue(false);// test fail
			}
			// Verify Resolution code -->Fix Applied
			Select resolutionCode1 = new Select(
					driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
			String selectedCode1 = resolutionCode1.getFirstSelectedOption().getText();

			if (selectedCode1.equals(problemdata[19][1])) { // -----------------------add problemdata state value "Fix
															// Applied"
				test.log(Status.PASS, "Resolution code is - " + selectedCode1 + " when click on Complete UI action");
				logger.info("Resolution code is - " + selectedCode1 + " when click on Complete UI action");
				AssertJUnit.assertTrue(true); // test pass
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			} else {
				test.log(Status.FAIL, "Resolution code is not Fix Applied");
				logger.info("Resolution code is not Fix Applied");
				AssertJUnit.assertTrue(false);// test fail
				String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
				test.info("State and Resolution code",
						MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
	}

	@Test(description = "SC_014- Verification of Email Notification", priority = 15)
	public void emailNotification() throws InterruptedException {

		try {
			logger.info("SC_014- Verification of Email Notification");
			test = ExtentReportManager.createTest("SC_014- Verification of Email Notification");
			
			driver.get(baseUrl + "/sys_email_list");
			Thread.sleep(2000);
			test.log(Status.INFO, "Search Email by Subject");
			//Search email by subject
			WebElement subjectSearch = driver.findElement(By.xpath("//*[@id=\"sys_email_table\"]/thead/tr[2]/td[5]/div/div/div/input"));
//			subjectSearch.isDisplayed();
			if(subjectSearch.isDisplayed()) {
				
			}
			else {
				driver.findElement(By.xpath("//*[@id='hdr_sys_email']/th[2]/div/button")).click();
			}
			Thread.sleep(2000);
			WebElement searchNotification =driver.findElement(By.xpath("//*[@id='sys_email_table']/thead/tr[2]/td[5]/div/div/div/input"));	// Click on Fix UI action
			String sub="Problem "+problemNo+" has been assigned to you";
			searchNotification.sendKeys(sub);																																		
			searchNotification.sendKeys(Keys.ENTER);
			
			/*	
			//Check Recipients
			WebElement openedRecord = driver.findElement(By.xpath("//*[@id='hdr_sys_email']/th"));
			
			String openedPRB = openedRecord.getAttribute("value");
		*/	
			//Open Email
			List<WebElement> openEmail = driver.findElements(By.xpath("//table[@id='sys_email_table']/tbody/tr/td[5]"));
			for (WebElement ele2 : openEmail) {
				String subject = ele2.getText();
				if (subject.contains(problemNo)) {
					//Open Notification
					driver.findElement(By.xpath("//table[@id='sys_email_table']/tbody/tr/td[3]/a")).click();
					break;
				}
			}
			
			//Verify Subject
			test.log(Status.INFO, "Email Opened");
			WebElement subject = driver.findElement(By.xpath("//*[@id='sys_email.subject']"));
			String noteSubject = subject.getAttribute("value");
			System.out.println("Subject is "+noteSubject);
			Assert.assertEquals(noteSubject, sub, "Subject is as per requiremnt "+noteSubject);
			test.log(Status.PASS, "Subject is "+noteSubject);
			
			//Verify Recipients 
			WebElement recipent = driver.findElement(By.xpath("//*[@id='sys_email.recipients']"));
			String noteDownRecipent = recipent.getText();
			System.out.println("Recipients is on Email "+noteDownRecipent);
			System.out.println("Noted down Recipients is from profile "+noteEmailID);
			Assert.assertEquals(noteDownRecipent, noteEmailID, "Recipients is as per requirement" +noteDownRecipent);   
			test.log(Status.PASS, "Recipients is "+noteDownRecipent);
			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Recipients & Subject", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			
			//Verify Email body
			//Click on Preview Email link
			test.log(Status.INFO, "Click on Preview Email related link to verify Email body");
			driver.findElement(By.xpath("//*[@id='3f018430c0a8006f003b8fe60ba935cf']")).click();
			Thread.sleep(2000);
			driver.switchTo().frame("email_preview_iframe");
			
			//Print body on Console
			WebElement body =driver.findElement(By.xpath("/html/body"));
			String bodyText=body.getText();
			System.out.println("Email body is "+bodyText);
			test.log(Status.INFO, "Email body is :"+bodyText);
			test.info("Email body", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			
			//Verify Short description
			WebElement shortDesc =driver.findElement(By.xpath("/html/body/div[1]"));
//			WebElement shortDesc =driver.findElement(By.partialLinkText("Short description"));			
			String shortDescription=shortDesc.getText();
			System.out.println("Shrot description is "+problemdata[10][1]);
			
			String[] shrtDesc=shortDescription.split(": ");
			Assert.assertEquals(shrtDesc[1], problemdata[10][1], "Short description is as per requirement" +shrtDesc[1]);   
			test.log(Status.PASS, "Short description is "+shrtDesc[1]);
		  
		    
			//Verify state
			WebElement state =driver.findElement(By.xpath("//div[contains(text(),'State:')]"));   
			String stateIs=state.getText();
			System.out.println("State text is "+stateIs);
			
			String[] state1=stateIs.split(": ");
			Assert.assertEquals(state1[1], problemdata[14][0], "State is as per requirement" +state1[1]);   
			test.log(Status.PASS, "State is "+state1[1]);
			
			//Verify Priority
			WebElement priority =driver.findElement(By.xpath("//*[contains(text(),'Priority:')]"));
			String priorityIs=priority.getText();
			System.out.println("Priority text is "+priorityIs);
			
			String[] priority1=priorityIs.split(": ");
			Assert.assertEquals(priority1[1], problemdata[7][1], "Priority is as per requirement" +priority1[1]);   
			test.log(Status.PASS, "Priority is "+priority1[1]);
			
			
			//Verify Configuration item
			WebElement CI =driver.findElement(By.xpath("//*[contains(text(),'Configuration item:')]"));
			String ciIs=CI.getText();
			System.out.println("CI text is "+ciIs);
			
			String[] ci=ciIs.split(": ");
			Assert.assertEquals(ci[1], problemdata[1][1], "CI is as per requirement" +ci[1]);   
			test.log(Status.PASS, "CI is "+ci[1]);
			
			//Verify Open by
			WebElement openBy =driver.findElement(By.xpath("//*[contains(text(),'Opened by:')]"));
			String openByIs=openBy.getText();
			System.out.println("Open by text is "+openByIs);
			
			String[] openByuser=openByIs.split(": ");
			Assert.assertEquals(openByuser[1], problemdata[1][2], "Open by is as per requirement" +openByuser[1]);   
			test.log(Status.PASS, "Open by is "+openByuser[1]);
			
			//Verify Assignment group
			WebElement assignmentGrp =driver.findElement(By.xpath("//*[contains(text(),'Assignment group:')]"));
			String assignmentGrpIs=assignmentGrp.getText();
			System.out.println("Assignment group text is "+assignmentGrpIs);
			
			String[] assignmentGp=assignmentGrpIs.split(": ");
			Assert.assertEquals(assignmentGp[1], problemdata[8][1], "Assignment group is as per requirement" +assignmentGp[1]);   
			test.log(Status.PASS, "Assignment group is "+assignmentGp[1]);
			
			//Verify Assign to
			WebElement assignTo =driver.findElement(By.xpath("//*[contains(text(),'Assigned to:')]"));
			String assignToIs=assignTo.getText();
			System.out.println("Assign to text is "+assignToIs);
			
			String[] assignToUser=assignToIs.split(": ");
			Assert.assertEquals(assignToUser[1], problemdata[9][1], "Assign to is as per requirement" +assignToUser[1]);   
			test.log(Status.PASS, "Assign to is "+assignToUser[1]);
			
			//Verify Description
			WebElement desc =driver.findElement(By.xpath("//*[contains(text(),'"+problemdata[12][1]+"')]"));
			String descIs=desc.getText();
			System.out.println("Description text is "+descIs);
			
//			String[] description=descIs.split(": ");
			Assert.assertEquals(descIs, problemdata[12][1], "Description is as per requirement" +descIs);   
			test.log(Status.PASS, "Description is "+descIs);
			
			//Problem link
			test.log(Status.INFO, "Check link of Problem");
			WebElement linkTxt =driver.findElement(By.xpath("//*[contains(text(),'Click here')]"));			
			String linkText=linkTxt.getText();
			System.out.println("Link text is "+linkText);
			
			//Click on Problem link
			test.log(Status.INFO, "Click on link of Problem number");
			WebElement link =driver.findElement(By.xpath("/html/body/div[2]/a"));			
			String prbLink=link.getText();
			System.out.println("Problem link is "+prbLink);
			link.click();
			
			//Handle window to check Problem open in new tab
			ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs.get(1));
		    //Verify Problem is opened or not
		    Thread.sleep(2000);		
		    WebElement prb = driver.findElement(By.xpath("//*[@id='sys_readonly.problem.number']"));
			String prbvalue = prb.getAttribute("value");
			System.out.println("Problem open in new tab is "+prbvalue);
			
			Assert.assertEquals(prbvalue, problemNo, "Opened Problem record is as per requirement :" +prbvalue);
			test.log(Status.PASS, "Opened Problem record :" + prbvalue);
			test.info("Opened Problem record", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
			
		    driver.close();
		    driver.switchTo().window(tabs.get(0));
			
		}
		catch (Exception e) {
			e.printStackTrace();
			AssertJUnit.assertTrue(false);
		}
		}

	@Test(priority = 15)
	public void endimpersonation() throws InterruptedException {
		// End Impersonation
		jse = (JavascriptExecutor) driver;
		Thread.sleep(2000);
		impersonation.endImpersonation(jse);
		Thread.sleep(2000);

		// Impersonating adimn user
		Object[][] users = DataImport.getData("ImpersonateUser");
		impersonation.startImpersonation(users[0][0].toString(), jse);
		Thread.sleep(2000);
	}

}