package com.example.tests;


import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import base.BaseTest;
import base.DriverManager;
import base.Navigator;
import junit.framework.Assert;
import utils.DataImport;
import utils.ExtentReportManager;
import utils.Log;


public class Problem extends BaseTest {
	private WebDriver driver = DriverManager.getDriver(); 
	String changeNo;
	private JavascriptExecutor jse;
	private Logger logger = LogManager.getLogger("Problem");
	Object[][] problemdata = DataImport.getData("Problem");
	
	private Navigator navigator;

//		@Test
//		public void impersonateUser() {
//
//			logger.info("Impersonating to User");
//			test = ExtentReportManager.createTest("Impersonate to User");
//
//			String baseUrl = "https://dev231719.service-now.com";
//
//			// Click on profile logo
//			test.log(Status.INFO, "Click on Profile Logo");
//			WebElement clickProfile = (WebElement) jse.executeScript("return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span > img\")");
//			((JavascriptExecutor) driver).executeScript("arguments[0].click();", clickProfile);
//
//			test.log(Status.INFO, "Click on Impersonate User");
//			// Click on Impersonate
//			WebElement impersonateUser = (WebElement) jse.executeScript("return document.querySelector('body > macroponent-f51912f4c700201072b211d4d8c26010').shadowRoot.querySelector('div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout').shadowRoot.querySelector('div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header').shadowRoot.querySelector('#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div')");
//			((JavascriptExecutor) driver).executeScript("arguments[0].click();", impersonateUser);
//
//			Thread.sleep(2000);
//
//			// Enter user name
//			test.log(Status.INFO, "Enter User Name");
//			WebElement searchUser = (WebElement) jse.executeScript("return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\").shadowRoot.querySelector(\"div > now-popover > div\")");
//			Thread.sleep(2000);
//			WebElement textBox = searchUser.findElement(By.cssSelector(".now-typeahead-native-input"));
//			textBox.sendKeys("Fred Luddy");                                                                //  ---------------Data input 
//			Thread.sleep(2000);
//			textBox.sendKeys(Keys.ENTER);
//
//			// Click on Impersonate button
//			test.log(Status.INFO, "Click on Impersonate button");
//			WebElement impersonateBtn = (WebElement) jse.executeScript("return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button\")");
//			((JavascriptExecutor) driver).executeScript("arguments[0].click();", impersonateBtn);
//
//			// Verify user is Impersonated or not
//		}

		@Test(description = "Verification of Save record without filling mandatory fields")
		public void submitProblemFormWODetail() {
			logger.info("Open and Submitting Problem form without fill details in mandatory fields");
			test = ExtentReportManager.createTest("Open and Submitting Problem form without fill details in mandatory fields");
			
			//Open problem table
			navigator = new Navigator(driver);
			
	    	navigator.allNavigation("problem.list", jse);
			
	    	//Click on the New button
	    	test.log(Status.INFO, "Open Problem table and click on New button to open Problem form");
	    	navigator.newUIAction(jse);
	    	
	    	test.log(Status.INFO, "Save record without fill details in mandatory fields");
			logger.info("Save record without fill details in mandatory fields");
			// Click on Submit button
			driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();

			// Verify Error message

			// Verify mandatory fields
			// Assigned to field is mandatory
			// *[@id="status.problem.assigned_to"]
			// Problem statement is mandatory
			// *[@id="status.problem.short_description"]

		}

		@Test
		public void validateProblemForm() {
			
			logger.info("Validate and fill field details");
			test = ExtentReportManager.createTest("Validate and fill field details");

			//Verify Original task field
			test.log(Status.INFO, "Verifying Original task field");
			
			String originalTaskField = driver.findElement(By.xpath("//*[@id=\"label.problem.first_reported_by_task\"]/label/span[2]")).getAttribute("value"); //---------------add xpath of field
			if (originalTaskField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Original task' field name as expected");
				logger.info("'Original task' field name as expected");
				
				// **Set original task field value**
				String mainWindowHandle = driver.getWindowHandle();
				System.out.println("Main Window Handle: " + mainWindowHandle);
				test.log(Status.INFO, "Set original task field value");
				logger.info("Set original task field value");
				// Click on original task look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.first_reported_by_task']")).click();
				Thread.sleep(2000);

				// Get all window handles after the new window opens
				Set<String> allWindowHandles = driver.getWindowHandles();
				System.out.println("All Window Handles: " + allWindowHandles.size());

				// Switch to child opened window Set<String> allWindowHandles
				Iterator<String> iterator = allWindowHandles.iterator();
				while (iterator.hasNext()) {
					String childWindowHandle = iterator.next();

					// Switch to the child window only if it's not the main window
					if (!mainWindowHandle.equalsIgnoreCase(childWindowHandle)) {
						driver.switchTo().window(childWindowHandle);
						System.out.println("Switched to child window with title: " + driver.getTitle());
						// Select change from table
						driver.findElement(By.xpath("//table[@id='task_table']/tbody/tr[2]/td[3]")).click();
						break; // Exit the loop once the correct window is found/switched
					}
				}

				// Switch back to the main window
				driver.switchTo().window(mainWindowHandle);
				System.out.println("Switched back to main window with title: " + driver.getTitle());
				
				//Print set value
				String originalTaskValue = driver.findElement(By.xpath("")).getText(); //-----------------------Add entered value in Original task
				
				test.log(Status.INFO, "Set Original task field value is - " +originalTaskValue );
				logger.info("Set Original task field value is - " +originalTaskValue );
				
				
			} else {
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.FAIL, "Original task field is not expected");
				logger.info("Original task field is not expected");
			}
			
			
			//Verify Category field
	       test.log(Status.INFO, "Verifying Category field");
			
			String categoryField = driver.findElement(By.id("username")).getAttribute("value"); //---------------add xpath of field
			if (categoryField.equals(problemdata[0][0])) {                                   // -----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Category' field name as expected");
				logger.info("'Category' field name as expected");
				
				// **Set Category field value**
				test.log(Status.INFO, "Select Category field value");
				logger.info("Select Category field value");
				
				WebElement selectCategory = driver.findElement(By.xpath("//*[@id='problem.category']")); // Locate the
				Select dropdown = new Select(selectCategory); // Create an instance of Select class
//						dropdown.selectByVisibleText("Duplicate"); // Select option by visible text
				dropdown.selectByIndex(2); // Selects the second option (index starts from 0) // Select option by index
				
				//Print set value
				String categoryValue = driver.findElement(By.xpath("")).getText(); //-----------------------Add entered value in Category
				
				test.log(Status.INFO, "Selected Category field value is - " + categoryValue);
				logger.info("Selected Category field value is - " + categoryValue);
				
				
			} else {
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "Category field is not expected");
				logger.info("Category field is not expected");
			}
			
			
			//Verify Subcategory field
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying Subcategory field");
			
			String subCategoryField = driver.findElement(By.id("username")).getAttribute("value"); //---------------add xpath of field
			if (subCategoryField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Subcategory' field name as expected");
				logger.info("'Subcategory' field name as expected");
				
				// **Set Subcategory field value**
				test.log(Status.INFO, "Select Subcategory field value");
				logger.info("Select Subcategory field value");
				
				WebElement dropdownElement = driver.findElement(By.xpath("//*[@id='problem.subcategory']")); // Locate the
				Select subCategoryEle = new Select(dropdownElement); 
				subCategoryEle.selectByIndex(2); // Selects the second option (index starts from 0) // Select option by index
				//Print set value
				String subCategoryValue = driver.findElement(By.xpath("")).getText();           //-----------------------Add entered value in Subcategory
				
				test.log(Status.INFO, "Selected Subcategory field value is - " + subCategoryValue);
				logger.info("Selected Subcategory field value is - " + subCategoryValue);
				
				
			} else {
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "Subcategory field is not expected");
				logger.info("Subcategory field is not expected");
			}
			
			
			//Verify Impact field value**
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying Impact field");
			
			String impactField = driver.findElement(By.id("username")).getAttribute("value"); //---------------add xpath of field
			if (impactField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Impact' field name as expected");
				logger.info("'Impact' field name as expected");
			
			// **Set Impact field value**
			test.log(Status.INFO, "Select Impact field value");
			logger.info("Select Impact field value");
			
			WebElement selectImpact = driver.findElement(By.xpath("//*[@id='problem.impact']")); 
			Select dropdown1 = new Select(selectImpact); 
			dropdown1.selectByIndex(0); 
			//Print set value
			String impactValue = driver.findElement(By.xpath("")).getText();           // -----------------------Add entered value in Subcategory
			
			test.log(Status.INFO, "Selected Impact field value is - " + impactValue);
			logger.info("Selected Impact field value is - " + impactValue);
		} 
		else{
			AssertJUnit.assertTrue(false);//test case failed
			test.log(Status.INFO, "Impact field is not expected");
			logger.info("Impact field is not expected");
		}
		
		// Verify Urgency field 
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying Urgency field");
			
			String urgencyField = driver.findElement(By.id("username")).getAttribute("value"); //---------------add xpath of field
			if (urgencyField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Urgency' field name as expected");
				logger.info("'Urgency' field name as expected");
			
			// **Set Urgency field value**
			test.log(Status.INFO, "Select Urgency field value");
			logger.info("Select Urgency field value");
			
			WebElement selectUrgency = driver.findElement(By.xpath("//*[@id='problem.urgency']")); 
			Select dropdown2 = new Select(selectUrgency); 
			dropdown2.selectByIndex(1); 
			//Print set value
			String urgencyValue = driver.findElement(By.xpath("")).getText();     //-----------------------Add entered value in Subcategory
			
			test.log(Status.INFO, "Selected Urgency field value is - " + urgencyValue);
			logger.info("Selected Urgency field value is - " + urgencyValue);
		} 
		else{
			AssertJUnit.assertTrue(false);//test case failed
			test.log(Status.INFO, "Urgency field is not expected");
			logger.info("Urgency field is not expected");
		}
		
			// Verify Priority field 
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying Priority field and value");
	        
			String priorityField = driver.findElement(By.xpath("//*[@id='label.problem.priority']/label/a/span[2]")).getAttribute("value"); 
			if (priorityField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Priority' field is visible");
				logger.info("'Priority' field visible");
			}
			else{
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "Priority field is not visible");
				logger.info("Priority field is not visible");
			}
			//Verify Priority field value
			String priorityValue = driver.findElement(By.xpath("//*[@id='problem.priority']")).getText();   
			if (priorityValue.equals(problemdata[0][4])) {
				test.log(Status.INFO, "Priority is - "+priorityValue+ " as per selected Impact - " + impactValue+ " and Urgency - " + urgencyValue);
				logger.info("Priority is - "+priorityValue +" as per selected Impact - " + impactValue+ " and Urgency - " + urgencyValue);
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("Priority is not as per selected Impact and Urgency");
				AssertJUnit.assertTrue(false);//test fail
			}
			
			
			//Verify Problem statement field
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying Problem statement field");
			
			String problemStmntField = driver.findElement(By.id("username")).getAttribute("value"); //---------------add xpath of field
			if (problemStmntField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Problem statement' field name as expected");
				logger.info("'Problem statement' field name as expected");
			
			// Enter Problem statement field details
			test.log(Status.INFO, "Enter Problem statement field value");
			logger.info("Enter Problem statement field value");
			
			driver.findElement(By.xpath("//*[@id='problem.short_description']")).sendKeys(problemdata[0][0]);   //-------add excel field value
			
			//Print set value
			String problemStmntValue = driver.findElement(By.xpath("")).getText(); //-----------------------Add entered value in Problem statement
			
			test.log(Status.INFO, "Entered Problem statement field value is - " + problemStmntValue);
			logger.info("Entered Problem statement field value is - " + problemStmntValue);
		} 
		else{
			AssertJUnit.assertTrue(false);//test case failed
			test.log(Status.INFO, "Problem statement field is not expected");
			logger.info("Problem statement field is not expected");
		}
			
			//Verify Assignment group field	
	        test.log(Status.INFO, "Verifying Assignment group field");
			
			String originalTaskField = driver.findElement(By.xpath("//*[@id='label.problem.assignment_group']/label/span[2]")).getAttribute("value");
			
			if (originalTaskField.equals(testData[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Assignment group' field name as expected");
				logger.info("'Assignment group' field name as expected");
				
				// **Set Assignment group field value**
				String mainWindowHandle1 = driver.getWindowHandle();
				System.out.println("Main Window Handle: " + mainWindowHandle1);
				
				test.log(Status.INFO, "Set Assignment group field value");
				logger.info("Set Assignment group field value");
				// Click on Assignment group look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.assignment_group']")).click();
				Thread.sleep(2000);

				// Get all window handles after the new window opens
				Set<String> allWindowHandles1 = driver.getWindowHandles();
				System.out.println("All Window Handles: " + allWindowHandles1.size());
				
				// Switch to child opened window Set<String> allWindowHandles
				Iterator<String> iterator1 = allWindowHandles1.iterator();
				while (iterator1.hasNext()) {
					String childWindowHandle1 = iterator1.next();

					// Switch to the child window only if it's not the main window
					if (!mainWindowHandle1.equalsIgnoreCase(childWindowHandle1)) {
						driver.switchTo().window(childWindowHandle1);
						System.out.println("Switched to child window with title: " + driver.getTitle());
						// Search assignment group
						WebElement searchAssignmentGroup = driver
								.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
						searchAssignmentGroup.sendKeys(problemdata[0][0] + Keys.ENTER);                //------------------"Problem Analyzers" get value from excel
						Thread.sleep(3000);

						// Select assignment group
						driver.findElement(By.xpath("//table[@id='sys_user_group_table']/tbody/tr[1]/td[3]/a")).click();
						break; 				}
				}
				// Switch back to the main window
				driver.switchTo().window(mainWindowHandle1);
				System.out.println("Switched back to main window with title: " + driver.getTitle());
				
				//Print set value
				String assignmentGroupValue = driver.findElement(By.xpath("")).getText();         //-----------------------Add entered value in Assignment group
				
				test.log(Status.INFO, "Set Assignment group field value is - " +assignmentGroupValue );
				logger.info("Set Assignment group field value is - " +assignmentGroupValue );
				
			} 
			else{
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "Assignment group field is not expected");
				logger.info("Assignment group field is not expected");
			}
			
			//Verify Assign to field 
	        test.log(Status.INFO, "Verifying Assign to field");
			
			String originalTaskField = driver.findElement(By.xpath("//*[@id='label.problem.assigned_to']/label/span[2]")).getAttribute("value");
			if (originalTaskField.equals(problemdata[0][0])) {                                    //-----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'Assign to' field name as expected");
				logger.info("'Assign to' field name as expected");
				
				// Set Assign to field value
				String mainWindowHandle2 = driver.getWindowHandle();
				System.out.println("Main Window Handle: " + mainWindowHandle2);
				
				test.log(Status.INFO, "Select Assign to user in field");
				logger.info("Select Assign to user in field");
				// Click on Assign to look up
				driver.findElement(By.xpath("//*[@id='lookup.problem.assigned_to']")).click();
				Thread.sleep(2000);

				// Get all window handles after the new window opens
				Set<String> allWindowHandles2 = driver.getWindowHandles();
				System.out.println("All Window Handles: " + allWindowHandles2.size());

				// Switch to child opened window Set<String> allWindowHandles
				Iterator<String> iterator2 = allWindowHandles2.iterator();
				while (iterator2.hasNext()) {
					String childWindowHandle2 = iterator2.next();

					// Switch to the child window only if it's not the main window
					if (!mainWindowHandle2.equalsIgnoreCase(childWindowHandle2)) {
						driver.switchTo().window(childWindowHandle2);
						System.out.println("Switched to child window with title: " + driver.getTitle());
						// Search assign to
						WebElement searchAssigTo = driver
								.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
						searchAssigTo.sendKeys(problemdata[0][0] + Keys.ENTER);                      //-------------------------'Problem Manager' get value from excel

						Thread.sleep(2000);
						// Select assign to
						driver.findElement(By.xpath("//table[@id='sys_user_table']/tbody/tr[1]/td[3]")).click();
						break; 
					}
				}

				// Switch back to the main window
				driver.switchTo().window(mainWindowHandle2);
				System.out.println("Switched back to main window with title: " + driver.getTitle());
				
				//Print set value
				String assignToalue = driver.findElement(By.xpath("")).getText();       //-----------------------Add entered value in Original task
				
				test.log(Status.INFO, "Selected Assign to field value is - " +assignToalue);
				logger.info("Selected Assign to field value is - " +assignToalue);
				
			} 
			else{
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "Assign to field is not expected");
				logger.info("Assign to field is not expected");
			}
			
			
			// Verify State field name
			Thread.sleep(2000);
	        test.log(Status.INFO, "Verifying State field and value");
	        
			String stateField = driver.findElement(By.xpath("//*[@id='label.problem.state']/label/span[2]")).getAttribute("value"); 
			if (stateField.equals(problemdata[0][0])) {                                   // -----------------add excel field name path
				AssertJUnit.assertTrue(true); //test case pass
				test.log(Status.INFO, "'State' field is visible");
				logger.info("'State' field visible");
			}
			else{
				AssertJUnit.assertTrue(false);//test case failed
				test.log(Status.INFO, "State field is not visible");
				logger.info("State field is not visible");
			}
			//Verify State field value
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[0][0])) {                                //-----------------------add excel field value "New"
				test.log(Status.INFO, "State is - "+stateValue);
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("State is not New");
				AssertJUnit.assertTrue(false);//test fail
			}
		}

		@Test
		public void submitForm() {

			logger.info("Save form once fill all required details");
			test = ExtentReportManager.createTest("Save form once fill all required details");

			// Note down problem number
			test.log(Status.INFO, "Note down Problem number");
			WebElement inputElement = driver.findElement(By.xpath("//*[@id='sys_readonly.problem.number']"));
			String value = inputElement.getAttribute("value");
			test.log(Status.INFO, "Problem number is - " + value);
			logger.info("Problem number is - " + value);

			// Click on Save UI action
			test.log(Status.INFO, "Click on Save UI action");
			driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();

		}

		@Test
		public void reOpenForm() {

			logger.info("Reopen submitted record from Problem table");
			test = ExtentReportManager.createTest("Reopen submitted record from Problem table");

			// Search Problem record on table
			test.log(Status.INFO, "Search Problem number");
			WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
			globalSearchBox.sendKeys(value);
			Thread.sleep(1000);
			globalSearchBox.sendKeys(Keys.ENTER);
			Thread.sleep(2000);

			// Open Problem
			test.log(Status.INFO, "Click and open created record");
			List<WebElement> openPRB = driver.findElements(By.xpath("//table[@id='problem_table']/tbody/tr/td[3]/a"));
			for (WebElement ele2 : openPRB) {
				String currentPRB = ele2.getText();
				System.out.println(currentPRB);
				if (currentPRB.contains(value)) {
					ele2.click();
					break;
				}
			}
			// Verify Opened Problem record
			test.log(Status.INFO, "Verify opened record");

			WebElement openedRecord = driver.findElement(By.xpath("//*[@id='sys_readonly.problem.number']"));
			String openedPRB = openedRecord.getAttribute("value");
			test.log(Status.INFO, "Opened Problem record is - " + openedPRB);
			logger.info("Opened Problem record is - " + openedPRB);		
			Assert.assertEquals(openedPRB, value, "Created Problem is opened");
		}
		
		@Test
		public void stateOnceAssignSave() {

			logger.info("Verify State once Problem is Assigned");
			test = ExtentReportManager.createTest("Verify State once Problem is Assigned");
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[0][0])) {                                -----------------------add excel field value "Assess"
				test.log(Status.INFO, "State is - "+stateValue);
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("State is not Assess");
				AssertJUnit.assertTrue(false);//test fail
			}
		
		}
		
		@Test
		public void RCAState() {

			logger.info("Verify State once Problem is Confirmed");
			test = ExtentReportManager.createTest("Verify State once Problem is Cinfirm");
			
			// Click on confirm UI action
			WebElement confirmUI = driver.findElement(By.xpath("//*[@id=\"move_to_rca\"]"));
			String uiAction = confirmUI.getText();
			boolean uiAction = confirmUI.isDisplayed();
			if (uiAction) {
				
				confirmUI.click();
				test.log(Status.INFO, "Confirm UI action is visible");
				logger.info("Confirm UI action is visible");
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("Confirm UI action is not visible");
				AssertJUnit.assertTrue(false);//test fail
			}
			
			//Verify State once click on Confirm UI action
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(Excel[0][0])) {                                -----------------------add excel state value "RCA"
				test.log(Status.INFO, "State is - "+stateValue);
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("State is not Root Cause Analysis");
				AssertJUnit.assertTrue(false);//test fail
			}
		
		}
		
		
		@Test
		public void fixStateWODetails() { //Negative scenario

			logger.info("Verify Problem if without fill required details for Fix");
			test = ExtentReportManager.createTest("Verify Problem if without fill required details for Fix");

			//Click on Fix UI action
			logger.info("Click on Fix UI action without fill required details");
			WebElement fixUI = driver.findElement(By.xpath("//*[@id='move_to_fix_in_progress']"));
			String fixUIAction = fixUI.getText();
			fixUI.click();
			
			//Close open dialog box- Refresh the current page
	        driver.navigate().refresh();
			Thread.sleep(2000);
			
			// Click on close opened mandatory field window
			WebElement element = driver.findElement(By.xpath("//*[@id='start_fix_dialog_form_view']/div/div/header/button"));
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;jsExecutor.executeScript("arguments[0].click();",element);
			
			
			//Verify State once click on Confirm UI action
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(Excel[0][0])) {                                -----------------------add excel state value "Fix in Progress"
				test.log(Status.INFO, "State is - "+stateValue ", Problem is not fixed");
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("State is Fixed");
				AssertJUnit.assertTrue(false);//test fail
			}
		
		}
		
		
		@Test
		public void fixState() {

			logger.info("Verify State once Problem is Fixed with filled mandtory field");
			test = ExtentReportManager.createTest("Verify State once Problem is Fixed with filled mandtory field");
			
			// Fill Cause Notes
			// Go to Analysis Information tab
			logger.info("Fill Cause Notes");
			driver.findElement(By.xpath("//*[@id='tabs2_section']/span[2]/span[1]/span[2]")).click();
			WebElement frameElement = driver.findElement(By.xpath(
					"//*[@id='problem.cause_notes_ifr']"));driver.switchTo().frame(frameElement);driver.findElement(By.xpath("//*[@id=\"tinymce\"]")).sendKeys("Test Cause Notes");

			// Fill Fix Notes
					logger.info("Fill Fix Notes");
			driver.switchTo().parentFrame();driver.findElement(By.xpath("//*[@id=\"tabs2_section\"]/span[3]/span[1]")).click();Thread.sleep(2000);
			WebElement frameElement1 = driver.findElement(By.xpath(
					"//*[@id='problem.fix_notes_ifr']"));driver.switchTo().frame(frameElement1);driver.findElement(By.xpath("//*[@id=\"tinymce\"]")).sendKeys("Test Fix Notes"); // ----------Failed
			driver.switchTo().parentFrame();

			
			// Click on Fix UI action
			WebElement fixUI = driver.findElement(By.xpath("//*[@id='move_to_fix_in_progress']"));
			String uiAction = fixUI.getText();
			boolean uiActionDisply = fixUI.isDisplayed();
			if (uiActionDisply) {
				fixUI.click();
				test.log(Status.INFO, "Fix UI action is visible");
				logger.info("Fix UI action is visible");
				AssertJUnit.assertTrue(true); //test pass
			} else {
				logger.info("Fix UI action is not visible");
				AssertJUnit.assertTrue(false);//test fail
			}
			
			//Verify State once click on Fix UI action
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[0][0])) {                               // -----------------------add excel state value "Fix in Progress"
				test.log(Status.INFO, "State is - "+stateValue);
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("State is not Fix in Progress");
				AssertJUnit.assertTrue(false);//test fail
			}
		
		}

		@Test
		public void resolveState() {

			logger.info("Verification of State and Resolution code once Problem is Resolved");
			test = ExtentReportManager.createTest("Verification of State and Resolution code once Problem is Resolved");
			
			WebElement resolveUI = driver.findElement(By.xpath("//*[@id='move_to_resolved']"));
			String uiAction = resolveUI.getText();
			boolean uiActionDisply = resolveUI.isDisplayed();
			if (uiActionDisply) {
				resolveUI.click();
				test.log(Status.INFO, "Resolve UI action is visible");
				logger.info("Resolve UI action is visible");
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("Resolve UI action is not visible");
				AssertJUnit.assertTrue(false);//test fail
			}
			
			//Verify State once click on Resolve UI action
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			if (stateValue.equals(problemdata[0][0])) {                               // -----------------------add excel state value "Resolve"
				test.log(Status.INFO, "State is - "+stateValue);
			logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("State is not Resolved");
				AssertJUnit.assertTrue(false);//test fail
			}
			// Verify Resolution code
			Select resolutionCode = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
			String selectedCode = resolutionCode.getFirstSelectedOption().getText();

			if (selectedCode.equals(problemdata[0][0])) {                                //-----------------------add excel state value "Fix Applied"
				test.log(Status.INFO, "Resolution code is - "+selectedCode);
			logger.info("Resolution code is - "+selectedCode);
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("Resolution code is not Fix Applied");
				AssertJUnit.assertTrue(false);//test fail
			}
		
		}	
		
		@Test
		public void reAnalyze() {

			logger.info("Verification of State when Problem is Re-Analyzed");
			test = ExtentReportManager.createTest("Verification of State when Problem is Re-Analyzed");

			WebElement reAnalyzeUI = driver.findElement(By.xpath("//*[@id='re_analyze']"));
			String uiAction = reAnalyzeUI.getText();
			boolean uiActionDisply = reAnalyzeUI.isDisplayed();
			if (uiActionDisply) {
				reAnalyzeUI.click();
				test.log(Status.INFO, "Re-Analyze UI action is visible");
				logger.info("Re-Analyze UI action is visible");
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("Re-Analyze UI action is not visible");
				AssertJUnit.assertTrue(false);//test fail
			}

		// Verify State of problem if click on Re-Analyze UI action -->Root Case Analysis
			Thread.sleep(2000);
			Select state = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
			String stateValue = state.getFirstSelectedOption().getText();

			Assert.assertEquals(stateValue, Excel[0][0], "State is - "+stateValue); -----------------------add excel state value "Root Case Analysis"
			if (stateValue.equals(Excel[0][0])) {                                -----------------------add excel state value "Root Case Analysis"
				test.log(Status.INFO, "State is - "+stateValue);
			    logger.info("State is - "+stateValue);
				AssertJUnit.assertTrue(true); //test pass
			} 
			else{
				logger.info("State is not Root Case Analysis");
				AssertJUnit.assertTrue(false);//test fail
			}
			
		}
		
		@Test
		public void cancelPRB() {

			logger.info("Verification of State when Problem is Cancelled");
			test = ExtentReportManager.createTest("Verification of State when Problem is Cancelled");
			
			// Click on Cancel UI action
			WebElement cancelUI = driver.findElement(By.xpath("//*[@id='cancel_problem']"));
			String uiAction = cancelUI.getText();
			boolean uiActionDisply = cancelUI.isDisplayed();
			if (uiActionDisply) {
				cancelUI.click();
				test.log(Status.INFO, "Cancel UI action is visible");
				logger.info("Cancel UI action is visible");
				AssertJUnit.assertTrue(true); //test pass
			} 
			else {
				logger.info("Cancel UI action is not visible");
				AssertJUnit.assertTrue(false);//test fail
			}
			Thread.sleep(2000);
			
	//Remaining to add assertions		
		
			
			
		// Click on close opened mandatory field window
		WebElement element = driver.findElement(By.xpath("//*[@id='tabs2_section']/span[4]/span[1]"));               //Check xpath
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;jsExecutor.executeScript("arguments[0].click();",element);

		Thread.sleep(2000);
		// Fill mandatory fields for Cancel ----> Close notes
		driver.findElement(By.xpath("//*[@id='cancel_dialog_form_view']/div/div/header/button")).click();

		driver.findElement(By.xpath("//*[@id='problem.close_notes']")).sendKeys("Test Close Notes");

		// Click on Cancel
		driver.findElement(By.xpath("//*[@id='cancel_problem']")).click();
		// Verify State of problem if click on Cancel UI action -->Complete
		Select stateCmplt = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
		String changedState = stateCmplt.getFirstSelectedOption().getText();
		if(changedState.equals("Closed"))
		{
			System.out.println("State is Closed");
		}else
		{
			System.out.println("State is not Closed");
		}

		// Verify Resolution code
		Select resolutionCode1 = new Select(
				driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
		String resolution = resolutionCode1.getFirstSelectedOption().getText();
		if(resolution.equals("Canceled"))
		{
			System.out.println("Resolution code is Canceled");
		}else
		{
			System.out.println("Resolution code is not Canceled");
		}
		// ---------------------------
		// Click on Re-Analyze UI action
		driver.findElement(By.xpath("//*[@id='re_analyze']")).click();

		// Verify State of problem if click on Re-Analyze UI action -->Root Case
		// Analysis
		Select stateRCA2 = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
		String stateText = stateRCA2.getFirstSelectedOption().getText();
		if(stateText.equals("Root Cause Analysis"))
		{
			System.out.println("State is Root Cause Analysis");
		}else
		{
			System.out.println("State is not Root Cause Analysis");
		}

		// -------------------
		// **Closing Problem
		// Click on Fix UI action
		driver.findElement(By.xpath("//*[@id=\"move_to_fix_in_progress\"]")).click();

		// Click on Resolve UI action
		driver.findElement(By.xpath("//*[@id=\"move_to_resolved\"]")).click();

		// Click on Complete UI action
		driver.findElement(By.xpath("//*[@id=\"move_to_closed\"]")).click();

		// Verify State is Closed
		Select stateClosed = new Select(driver.findElement(By.xpath("//*[@id='sys_readonly.problem.state']")));
		String stateTxt = stateClosed.getFirstSelectedOption().getText();
		if(stateTxt.equals("Closed"))
		{
			System.out.println("State is Closed");
		}else
		{
			System.out.println("State is not Closed");
		}

		// Verify Resolution code is Fixed Applied
		Select resolutionCode2 = new Select(
				driver.findElement(By.xpath("//*[@id='sys_readonly.problem.resolution_code']")));
		String selectedCode1 = resolutionCode2.getFirstSelectedOption().getText();
		if(selectedCode1.equals("Fix Applied"))
		{
			System.out.println("Resolution code is Fix Applied");
		}else
		{
			System.out.println("Resolution code is not Fix Applied");

		}

	}

	}

}