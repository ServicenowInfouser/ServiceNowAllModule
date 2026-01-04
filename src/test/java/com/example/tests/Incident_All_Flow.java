package com.example.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import base.Navigator;
import pages.IncidentPage;
import utils.ExtentReportManager;
import utils.DataImport;


public class Incident_All_Flow extends BaseTest {

	private WebDriver driver = DriverManager.getDriver(); 
	String createdInc;
	protected ExtentTest test;
	private JavascriptExecutor jse;
	private IncidentPage incidentpage = new IncidentPage(driver);
	private Navigator navigator = new Navigator(driver);


	@Test(priority=1, description = "Verification of Creation of Incident")
	public void create_Incident() throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Creation of Incident");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Incident_Flow");

		// Get value from excel cell 
		String user = excel_data[0][0].toString();
		String short_description = excel_data[0][1].toString();
		String new_state_Code = excel_data[0][2].toString().replaceAll("\\.0+$", "");

		// Navigate to All > incident table
		jse = (JavascriptExecutor) driver;   
		//navigator.allNavigation("incident.list", jse);
		//test.info("Navigate to All menu ");
		
		driver.get(BaseTest.baseUrl + "/incident_list");
		test.info("Open Incident table ");	

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Copy Incident record number
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
		createdInc = inputElement.getAttribute("value");
		test.info("Incident number : "+createdInc);

		// Validate caller fields is mandatory			
		incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

		// Select caller
		incidentpage.select_Caller(user,test);

		//Select Impact
		incidentpage.select_Impact("Medium",test);

		//Select Urgency
		incidentpage.select_Urgency("Medium",test);

		//Select Category
		incidentpage.select_Category("Hardware",test);

		//Select Sub category
		incidentpage.Select_Sub_Category("Monitor",test);

		// Validate short description fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

		// Enter short description
		incidentpage.enter_ShortDescription(short_description,test);

		//Capture screenshots
		String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);
		Thread.sleep(2000);
		driver.get(Config.baseUrl() + "/incident_list");	

		//Search Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		//Open Incident			
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is New
		incidentpage.verify_Incident_State(new_state_Code,test);				

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());		
	}


	@Test(dependsOnMethods = "create_Incident", description = "Verification that User is able to update the Incident record")
	public void update_Incident() throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification that User is able to update the Incident record");

		test.info("Incident number : "+createdInc);
		System.out.println("Upadte test started..");

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Incident before update : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		//Select Sub category
		incidentpage.Select_Sub_Category("CPU",test);

		// Enter short description
		driver.findElement(By.xpath("//*[@id='incident.short_description']")).clear();
		incidentpage.enter_ShortDescription("Test Updated Short Desctription ",test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);	

		//Search Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		//Open Incident			
		incidentpage.openIncident(createdInc,test);	

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Subcategory and Short Description Updated on Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
	}


	@Test(priority=2, description = "Verification of Creation of Incident for multiple users") 
	public void create_Incident_for_Multiple_Users () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Creation of Incident for multiple users");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Create_Inc");

		for (int i = 0; i < excel_data.length; i++) {

			// Get value from excel cell 
			String user = excel_data[i][0].toString();
			String short_description = excel_data[i][1].toString();
			String new_state_Code = excel_data[i][2].toString().replaceAll("\\.0+$", "");

			// Open incident table
			driver.get(Config.baseUrl() + "/incident_list");	
			test.info("Opening incident table");	

			//Capture screenshots
			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

			// Click on New button
			incidentpage.click_On_New_Button(test);

			//Capture screenshots
			String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

			// Copy Incident record number
			WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
			createdInc = inputElement.getAttribute("value");
			test.info("Incident number : "+createdInc);

			// Validate caller fields is mandatory			
			incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

			// Select caller
			incidentpage.select_Caller(user,test);

			//Select Impact
			incidentpage.select_Impact("Medium",test);

			//Select Urgency
			incidentpage.select_Urgency("Medium",test);

			//Select Category
			incidentpage.select_Category("Hardware",test);

			//Select Sub category
			incidentpage.Select_Sub_Category("Monitor",test);

			// Validate short description fields is mandatory
			incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

			// Enter short description
			incidentpage.enter_ShortDescription(short_description,test);

			//Capture screenshots
			String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

			// Click on submit
			incidentpage.click_On_Submit_Incident_Button(test);

			//Search Incident		
			incidentpage.searchIncident(createdInc,test);

			//Capture screenshots
			String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

			//Open Incident			
			incidentpage.openIncident(createdInc,test);	

			// Verify state of Incident is New
			incidentpage.verify_Incident_State(new_state_Code,test);				

			//Capture screenshots
			String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());
		}
		System.out.println("Incidents are Created !!");
		test.pass("Incidents are Created for All the Users. ");
	}


	@Test(priority=3, description = "Verification of Error Message on Incident") 
	public void validate_ErrorMessage_On_Incident () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Error Message on Incident");

		// Open incident table
		driver.get(Config.baseUrl() + "/incident_list");	
		test.info("Opening incident table");		

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);

		// validate error message
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: Short description, Caller",test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Error message validated on Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		System.out.println("Error message validated on Incident !!");
	}


	@Test	(priority=4, description = "Verification of Incident flow till On Hold state")
	public void on_Hold_Incident () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Incident flow till On Hold state");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Incident_Flow");

		// Get value from excel cell 
		String user = excel_data[0][0].toString();
		String short_description = excel_data[0][1].toString();
		String new_state_Code = excel_data[0][2].toString().replaceAll("\\.0+$", "");
		String in_Progress_Code = excel_data[0][3].toString().replaceAll("\\.0+$", "");
		String on_Hold_Code = excel_data[0][4].toString().replaceAll("\\.0+$", "");;

		// Open incident table
		driver.get(Config.baseUrl() + "/incident_list");	
		test.info("Opening incident table");		

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Copy Incident record number
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
		String createdInc = inputElement.getAttribute("value");
		test.info("Incident number : "+createdInc);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='sys_display.incident.caller_id']",test);

		// Validate caller fields is mandatory			
		incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

		// Select caller
		incidentpage.select_Caller(user,test);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.short_description']",test);

		// Validate short description fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

		// Enter short description
		incidentpage.enter_ShortDescription(short_description,test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);

		//Search Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());

		//Open Incident			
		incidentpage.openIncident(createdInc,test);		

		// Verify state of Incident is New
		incidentpage.verify_Incident_State(new_state_Code,test);

		//Capture screenshots
		String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

		// Change the State to In Progress
		incidentpage.change_Incident_state("In Progress",test);

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		// open incident
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is In Progress					
		incidentpage.verify_Incident_State(in_Progress_Code,test);		

		//Capture screenshots
		String screenshotPath5 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify Incident record is in state : In Progress ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath5).build());			

		// Change the State to On Hold
		incidentpage.change_Incident_state("On Hold",test);

		//Update the form without filling mandatory fields
		incidentpage.click_On_Update_Incident_Button(test);

		//Verify error message for mandatory fields for Incident		
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: On hold reason",test);
		Thread.sleep(2000);

		//Capture screenshots
		String screenshotPath6 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath6).build());

		// Verify On Hold Reason fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.hold_reason\"]/label/span[1]", "On Hold reason", test );

		// Select ON Hold reason > Awaiting for Change > Add reason code
		incidentpage.select_On_Hold_Reason_On_Incident("Awaiting Change",test);

		//Capture screenshots
		String screenshotPath7 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify error message and mandatory fields on state On Hold : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath7).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath8 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath8).build());

		// open incident
		incidentpage.openIncident(createdInc,test);	

		//Verify Incident state is On Hold
		incidentpage.verify_Incident_State(on_Hold_Code,test);	

		//Capture screenshots
		String screenshotPath9 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("State of Incident is On-Hold : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath9).build());
	}


	@Test(priority=5, description = "Verification of Incident flow till On Resolved state")
	public void resolved_Incident () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Incident flow till On Resolved state");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Incident_Flow");

		// Get value from excel cell 
		String user = excel_data[0][0].toString();
		String short_description = excel_data[0][1].toString();
		String new_state_Code = excel_data[0][2].toString().replaceAll("\\.0+$", "");
		String in_Progress_Code = excel_data[0][3].toString().replaceAll("\\.0+$", "");
		String on_Hold_Code = excel_data[0][4].toString().replaceAll("\\.0+$", "");
		String resolved_Code = excel_data[0][5].toString().replaceAll("\\.0+$", "");

		// Open incident table
		driver.get(Config.baseUrl() + "/incident_list");	
		test.info("Opening incident table");		

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Copy Incident record number
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
		String createdInc = inputElement.getAttribute("value");
		test.info("Incident number : "+createdInc);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='sys_display.incident.caller_id']",test);

		// Validate caller fields is mandatory			
		incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

		// Select caller
		incidentpage.select_Caller(user,test);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.short_description']",test);

		// Validate short description fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

		// Enter short description
		incidentpage.enter_ShortDescription(short_description,test);

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);

		//Search Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

		//Open Incident			
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is New
		incidentpage.verify_Incident_State(new_state_Code,test);

		//Capture screenshots
		String screenshotPath5 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath5).build());

		// Change the State to In Progress
		incidentpage.change_Incident_state("In Progress",test);

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		// open incident
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is In Progress					
		incidentpage.verify_Incident_State(in_Progress_Code,test);		

		//Capture screenshots
		String screenshotPath6 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify Incident record is in state : In Progress ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath6).build());			

		// Change the State to On Hold
		incidentpage.change_Incident_state("On Hold",test);

		//Update the form without filling mandatory fields
		incidentpage.click_On_Update_Incident_Button(test);

		//Verify error message for mandatory fields for Incident		
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: On hold reason",test);
		Thread.sleep(2000);

		//Capture screenshots
		String screenshotPath7 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath7).build());

		// Verify On Hold Reason fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.hold_reason\"]/label/span[1]", "On Hold reason",test );

		// Select ON Hold reason > Awaiting for Change > Add reason code
		incidentpage.select_On_Hold_Reason_On_Incident("Awaiting Change",test);

		//Capture screenshots
		String screenshotPath8 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify error message and mandatory fields on state On Hold : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath8).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath9 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath9).build());

		// open incident
		incidentpage.openIncident(createdInc,test);	

		//Verify Incident state is On Hold
		incidentpage.verify_Incident_State(on_Hold_Code,test);

		//Capture screenshots
		String screenshotPath10 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath10).build());

		//Resolve the Incident
		incidentpage.click_On_Resolved_Incident_Button(test);	

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);
		Thread.sleep(6000);

		//Capture screenshots
		String screenshotPath11 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath11).build());

		//Verify error message for mandatory fields for Incident on Resolved state
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: Resolution code, Resolution notes",test);		

		// CLick on Resolution info tab
		driver.findElement(By.xpath("//span[@class='tabs2_tab default-focus-outline tabs2_active']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		//Verify Resolution code is visible
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.close_code']",test);

		// verify resolution code is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.close_code\"]/label/span[1]", "Resolution Code",test);

		//Select resolution code
		incidentpage.select_Resolution_Code_On_Incident("Duplicate",test);

		//Verify Resolution note is visible
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.close_notes']",test);

		// Resolution note 
		incidentpage.verify_Field_Is_Mandatory("//label[@for='incident.close_notes']/span[1]", "Resolution Note",test);

		//Add note in resolution note
		driver.findElement(By.xpath("//textarea[@id='incident.close_notes']")).sendKeys("Testing add resolution note");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Added Resoltuon note : Testing add resolution note");

		//Capture screenshots
		String screenshotPath12 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Resolving Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath12).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath13 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath13).build());

		//Open incident
		incidentpage.openIncident(createdInc,test);			

		//Verify Incident is in Resolved state
		incidentpage.verify_Incident_State(resolved_Code,test);

		//Capture screenshots
		String screenshotPath14 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Resolved Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath14).build());
	}


	@Test(priority=6, description = "Verification of Incident flow till On Closed state")
	public void close_Incident () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Incident flow till On Closed state");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Incident_Flow");

		// Get value from excel cell 
		String user = excel_data[0][0].toString();
		String short_description = excel_data[0][1].toString();
		String new_state_Code = excel_data[0][2].toString().replaceAll("\\.0+$", "");
		String in_Progress_Code = excel_data[0][3].toString().replaceAll("\\.0+$", "");
		String on_Hold_Code = excel_data[0][4].toString().replaceAll("\\.0+$", "");
		String resolved_Code = excel_data[0][5].toString().replaceAll("\\.0+$", "");

		// Open incident table
		driver.get(Config.baseUrl() + "/incident_list");	
		test.info("Opening incident table");	

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Copy Incident record number
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
		String createdInc = inputElement.getAttribute("value");
		test.info("Incident number : "+createdInc);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='sys_display.incident.caller_id']",test);

		// Validate caller fields is mandatory			
		incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

		// Select caller
		incidentpage.select_Caller(user,test);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.short_description']",test);

		// Validate short description fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

		// Enter short description
		incidentpage.enter_ShortDescription(short_description,test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);

		//Search and open Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());

		//Open Incident	
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is New
		incidentpage.verify_Incident_State(new_state_Code,test);

		//Capture screenshots
		String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

		// Change the State to In Progress
		incidentpage.change_Incident_state("In Progress",test);

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath5 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath5).build());

		// open incident
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is In Progress					
		incidentpage.verify_Incident_State(in_Progress_Code,test);		

		//Capture screenshots
		String screenshotPath6 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify Incident record is in state : In Progress ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath6).build());			

		// Change the State to On Hold
		incidentpage.change_Incident_state("On Hold",test);

		//Update the form without filling mandatory fields
		incidentpage.click_On_Update_Incident_Button(test);

		//Verify error message for mandatory fields for Incident		
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: On hold reason",test);

		//Capture screenshots
		String screenshotPath7 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath7).build());

		// Verify On Hold Reason fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.hold_reason\"]/label/span[1]", "On Hold reason",test );

		// Select ON Hold reason > Awaiting for Change > Add reason code
		incidentpage.select_On_Hold_Reason_On_Incident("Awaiting Change",test);

		//Capture screenshots
		String screenshotPath8 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Verify error message and mandatory fields on state On Hold : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath8).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath9 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath9).build());

		// open incident
		incidentpage.openIncident(createdInc,test);	

		//Verify Incident state is On Hold
		incidentpage.verify_Incident_State(on_Hold_Code,test);

		//Capture screenshots
		String screenshotPath10 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath10).build());

		//Resolve the Incident
		incidentpage.click_On_Resolved_Incident_Button(test);	

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Verify error message for mandatory fields for Incident on Resolved state
		incidentpage.verifyIncidentErrorMessage("The following mandatory fields are not filled in: Resolution code, Resolution notes",test);		

		//Capture screenshots
		String screenshotPath11 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath11).build());

		// CLick on Resolution info tab
		driver.findElement(By.xpath("//span[@class='tabs2_tab default-focus-outline tabs2_active']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));

		//Verify Resolution code is visible
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.close_code']",test);

		// verify resolution code is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.close_code\"]/label/span[1]", "Resolution Code",test);

		//Select resolution code
		incidentpage.select_Resolution_Code_On_Incident("Duplicate",test);

		//Verify Resolution note is visible
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.close_notes']",test);

		// Resolution note 
		incidentpage.verify_Field_Is_Mandatory("//label[@for='incident.close_notes']/span[1]", "Resolution Note",test);

		//Add note in resolution note
		driver.findElement(By.xpath("//textarea[@id='incident.close_notes']")).sendKeys("Testing add resolution note");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		test.info("Added Resoltuon note : Testing add resolution note");

		//Capture screenshots
		String screenshotPath12 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Resolving Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath12).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath13 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath13).build());

		//Open incident
		incidentpage.openIncident(createdInc,test);			

		//Verify Incident is in Resolved state
		incidentpage.verify_Incident_State(resolved_Code,test);

		//Capture screenshots
		String screenshotPath14 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Resolved Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath14).build());

		//Close incident
		incidentpage.click_On_Closed_Incident_Button(test);

	}


	@Test(priority=7, description = "Verification of Incident flow till Canceled state")
	public void Cancel_Incident () throws InterruptedException {
		//Initialize report
		test = ExtentReportManager.createTest("Verification of Incident flow till Canceled state");

		// Create a object of getExcelData method
		Object[][] excel_data = DataImport.getData("Incident_Flow");

		// Get value from excel cell 
		String user = excel_data[0][0].toString();
		String short_description = excel_data[0][1].toString();
		String new_state_Code = excel_data[0][2].toString().replaceAll("\\.0+$", "");
		String canceled_Code = excel_data[0][7].toString().replaceAll("\\.0+$", "");

		// Open incident table
		driver.get(Config.baseUrl() + "/incident_list");	
		test.info("Opening incident table");	

		//Capture screenshots
		String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident table : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

		// Click on New button
		incidentpage.click_On_New_Button(test);

		//Capture screenshots
		String screenshotPath1 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath1).build());

		// Copy Incident record number
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='incident.number']"));
		String createdInc = inputElement.getAttribute("value");
		test.info("Incident number : "+createdInc);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='sys_display.incident.caller_id']",test);

		// Validate caller fields is mandatory			
		incidentpage.verify_Field_Is_Mandatory("//div[@id='label.incident.caller_id']/label/span[1]", "Caller",test);

		// Select caller
		incidentpage.select_Caller(user,test);

		//Validate field is visible on form
		incidentpage.verify_Field_Is_Visible("//label[@for='incident.short_description']",test);

		// Validate short description fields is mandatory
		incidentpage.verify_Field_Is_Mandatory("//div[@id=\"label.incident.short_description\"]/label/span[1]", "Short Description",test);

		// Enter short description
		incidentpage.enter_ShortDescription(short_description,test);

		//Capture screenshots
		String screenshotPath2 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath2).build());

		// Click on submit
		incidentpage.click_On_Submit_Incident_Button(test);

		//Search Incident		
		incidentpage.searchIncident(createdInc,test);

		//Capture screenshots
		String screenshotPath3 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath3).build());

		//Open Incident
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is New
		incidentpage.verify_Incident_State(new_state_Code,test);

		//Capture screenshots
		String screenshotPath4 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath4).build());

		// Change the State to Cancel
		incidentpage.change_Incident_state("Canceled",test);

		//Capture screenshots
		String screenshotPath5 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident form : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath5).build());

		//Update the form
		incidentpage.click_On_Update_Incident_Button(test);

		//Capture screenshots
		String screenshotPath6 = ExtentReportManager.captureScreenshot_new(driver);
		test.info("Incident in list view : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath6).build());

		// open incident
		incidentpage.openIncident(createdInc,test);	

		// Verify state of Incident is In Progress					
		incidentpage.verify_Incident_State_Canceled(canceled_Code,test);		

		//Capture screenshots
		String screenshotPath7 = ExtentReportManager.captureScreenshot_new(driver);
		test.pass("Canceled Incident : ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath7).build());
	}
}