
package com.example.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Test;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import junit.framework.Assert;
import utils.DataImport;
import utils.ExtentReportManager;


public class NormalChange extends BaseTest {
	private WebDriver driver = DriverManager.getDriver(); 
	String changeNo;
	private JavascriptExecutor jse;
	String firstAprovalUser=null, secondAprovalUser=null; 
		
	Object[][] changedata = DataImport.getData("Normal_Change");
	
	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);;
	
	/*
	 * @expected:User provided value	
	 * @actual: UI value
	 */
	public static void compareTwoStringsEquals(String expected,String actual) {
		Assert.assertEquals(expected, actual);
		
	}
    
    @Test(description = "Verification of Navigate to Change list")
    public void navigateToChangeList() throws InterruptedException {
    	Thread.sleep(5000);
    	jse = (JavascriptExecutor) driver;
    	test = ExtentReportManager.createTest("Verification of Navigate to Incident list");
    	
    	jse = (JavascriptExecutor) driver;    
    	//Navigation through all menu
    	test.info("Open Change list from All menu");
    	//navigator = new Navigator(driver);
    	navigator.allNavigation("change_request.list", jse);

    	test.info("Clicking on the New UI action");
    	//Click on the New UI action
    	navigator.newUIAction(jse);
        
        Thread.sleep(4000);
        //Click on Models tab
        WebElement models=driver.findElement(By.xpath("//*[@id='change_models']"));
        models.click();
        
        test.info("Clicking on the Normal change widget");
        Thread.sleep(5000);
        
        //Click on Normal widget
        WebElement NormalChange=driver.findElement(By.xpath("//*[@id='007c4001c343101035ae3f52c1d3aeb2']/div[1]/div[1]/span"));
        NormalChange.click();
        test.pass("Navigated to the New change page");
    }
    
    @Test(description = "Verification of Creation of change", dependsOnMethods = "navigateToChangeList")
    public void createChange() throws InterruptedException 
    {
    	String short_description = changedata[0][0].toString();
    	
    	test = ExtentReportManager.createTest("Verification of Navigating to Change list view");
    	
    	test.info("Verification of State");
    	Thread.sleep(2000);
    	String state1=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='New']")).getText();
    	System.out.println("State is:"+state1);
    	compareTwoStringsEquals("New", state1);
    	
    	// Copy Change record number
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='change_request.number']"));
        changeNo = inputElement.getAttribute("value");
        System.out.println("Change Number is:"+changeNo);
        
        Thread.sleep(2000);
        // Enter short description
        driver.findElement(By.xpath("//*[@id='change_request.short_description']")).sendKeys(short_description);
    
        test.info("verification of Submit UI Action");
        // Click on submit
        driver.findElement(By.xpath("//*[@id='sysverb_insert']")).click();
        
        test.pass("Change record created : "+changeNo);
        Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
    }
    
    @Test(description = "Verification of opening Created change record from list", dependsOnMethods = "createChange")
    public void openChange() throws InterruptedException {
    	test = ExtentReportManager.createTest("Opening Change record after Submition"); 
    	driver.get(Config.baseUrl() + "/change_request_list");
        Thread.sleep(2000);
        // Search Change record on table
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(changeNo);
        Thread.sleep(1000);
        globalSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        // Open Change
        List<WebElement> openCHN = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]/a"));
        for (WebElement ele2 : openCHN) {
             String currentINC = ele2.getText();
             if (currentINC.contains(changeNo)) 
             {
                 ele2.click();
                 break;
             }
        }
        test.pass("Change record opened from the list view");
        Reporter.getCurrentTestResult().setAttribute("TestData", changeNo);
    }
    
    @Test(description = "Verification of Approval Generation", dependsOnMethods = "openChange")
    public void requestingApproval() throws InterruptedException 
    {
    	String Assignmentgroup = changedata[0][1].toString();
    	String Assignto = changedata[0][2].toString();
    	Thread.sleep(10000);
    	
    	test = ExtentReportManager.createTest("Verification of Request Approval UI action");
    	//Click on RequestApproval UI action without fill Assignment group and Assign to fields
    	test.info("Click on the Request Approval UI action without fill Assignment group and Assign to fields");
        WebElement RequestApproval=driver.findElement(By.xpath("//*[@id='state_model_request_assess_approval']"));
        RequestApproval.click();
        
       //Verification of Error massage
        test.info("Verification of Error massage");
        Thread.sleep(2000);
        String errorMassage1=driver.findElement(By.xpath("//*[@id=\"output_messages\"]/div/div/span[2]")).getText();
        System.out.println("Error massage is :"+errorMassage1);

        // Set Group to Assignment group field
        test.info("Verification on fields on change record");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='sys_display.change_request.assignment_group']")).sendKeys(Assignmentgroup);
        
       // Set user to Assign to field
        test.info("Set user to Assign to field");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='sys_display.change_request.assigned_to']")).sendKeys(Assignto);
        
      //Click on RequestApproval UI action after fill Assignment group and Assign to fields
    	test.info("Click on the Request Approval UI action after fill Assignment group and Assign to fields");
    	Thread.sleep(2000);
        WebElement RequestApproval2=driver.findElement(By.xpath("//*[@id='state_model_request_assess_approval']"));
        RequestApproval2.click();

        //Click on the Approvers tab
        test.info("Go to the Approval Related list");
    	WebElement ApproversTab=driver.findElement(By.xpath("//*[@id='tabs2_list']/span[3]/span/span[2]"));
        ApproversTab.click();
        
        test.info("Verification of State after Click on the Approvers tab");
        String state2=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Assess']")).getText();
    	System.out.println("State is:"+state2);
    	compareTwoStringsEquals("Assess", state2);
        
        //Approval user
        List<WebElement> Approvers=driver.findElements(By.xpath("//*[@id='change_request.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]"));
        Thread.sleep(2000);
        
    
        if(Approvers.isEmpty()) {
        	System.out.println("No user found on the page.");
        }
        System.out.println("Total user found: "+Approvers.size());	
        	
        for(int i=0;i<Approvers.size();i++) {
        	String userName=Approvers.get(i).getText().trim();
        	test.info("Approver user " + Approvers.get(i).getText().trim());
        	System.out.println("User "+ (i+1)+ ":"+userName);
        	//store first userName
        	if(i==0) {
        		firstAprovalUser=userName;
        	}
        }
        System.out.println("First User Name is: "+firstAprovalUser);
        test.pass("Approvals generated successfully");
        
      //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", firstAprovalUser);
    }
    
    
    @Test(description = "Verification of User Impersonation", dependsOnMethods = "requestingApproval")
    public void impersonateUser() throws InterruptedException {
    	Thread.sleep(5000);
    	jse = (JavascriptExecutor) driver;
    	
    	//End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
    	test = ExtentReportManager.createTest("Verification of Approving the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	impersonation.startImpersonation(firstAprovalUser, jse);

    	Thread.sleep(2000);
        driver.get(baseUrl + "/sysapproval_approver_list");
        Thread.sleep(2000);
        
        test.info(" Search Change record in Approval table");
        WebElement Approvals=driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
        Approvals.click();
        Thread.sleep(3000);
        
        Select selectValu=new Select(Approvals);
        selectValu.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox2.sendKeys(changeNo);
        Thread.sleep(2000);
        globalSearchBox2.sendKeys(Keys.ENTER);
        
        WebElement approversearch=driver.findElement(By.xpath("//*[@id='sysapproval_approver_table']/thead/tr[2]/td[4]/div/div/div/input"));
        approversearch.sendKeys(firstAprovalUser);
        approversearch.sendKeys(Keys.ENTER);
       
        test.info("Opening Approval Change record");
        WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
        requestedbutton.click();
    
        test.info("Appriving the Approval");
        driver.findElement(By.xpath("//*[@id='approve']")).click(); 
        Thread.sleep(2000);
    	
        //End impersonation
    	test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
    	
    	//Impersonating adimn user
    	Object[][] users = DataImport.getData("ImpersonateUser");
    	impersonation.startImpersonation(users[0][0].toString(), jse);
    	Thread.sleep(2000);
    }
    
    
    //, dependsOnMethods = "impersonateUser"
    @Test(description = "Opening CHN record after first Approval Approve", dependsOnMethods = "impersonateUser")
    public void OPNCHNAFTAPPL() throws InterruptedException {
    	//changeNo= "CHG0030147";
    	
    	test = ExtentReportManager.createTest("Verification of Change record after 1st Approval Approved");
    	
    	System.out.println("1St Approval flow completed");
        //Opening Change record After 1st Approval
    	driver.get(Config.baseUrl() + "/change_request_list");
    	Thread.sleep(2000);
    	
    	// Search Change record on table
        WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox.sendKeys(changeNo);
        Thread.sleep(1000);
        globalSearchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);
        List<WebElement> openChange = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]/a"));
        for (WebElement chan1 : openChange) {
            String currentChan = chan1.getText();
            if (currentChan.contains(changeNo)) 
            {
          	  chan1.click();
                break;
            }
        }
    	
        System.out.println("Opening the Change record after 1St Approval");
        //Verification of 2nd Approval users
        test.info("2nd Approval users are");
        Thread.sleep(2000);
        
//        test.info("Go to the Approval Related list");
//    	WebElement ApproversTab=driver.findElement(By.xpath("//*[@id='tabs2_list']/span[3]/span/span[2]"));
//        ApproversTab.click();
//        System.out.println("t1");
//        
//        //WebElement Approvals2=driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[3]/span/div[2]/div[1]/div/span/div/div/span[1]/span/select"));
//        Thread.sleep(3000);
//        WebElement Approvals2=driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
//        Thread.sleep(3000);
//        Approvals2.click();
//        Thread.sleep(3000);
//        System.out.println("t2");
//        
//        Select selectValu=new Select(Approvals2);
//        selectValu.selectByVisibleText("State");
//        
//        Thread.sleep(2000);
//        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
//        globalSearchBox2.sendKeys("Requested");
//        Thread.sleep(2000);
//        globalSearchBox2.sendKeys(Keys.ENTER);
//        
        
        List<WebElement> Approvers2=driver.findElements(By.xpath("//*[@id='change_request.sysapproval_approver.sysapproval_table']/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println("Count of Approvers users are:"+Approvers2.size());
        for (WebElement Users2 : Approvers2) 
        {
            //secondAprovalUser = Users2.getText();
            System.out.println("List of Approver users are:"+Users2.getText());
        }
        
        
        for(int i=0;i<Approvers2.size();i++) {
        	String userName=Approvers2.get(i).getText().trim();
        	test.info("Approver user " + Approvers2.get(i).getText().trim());
        	System.out.println("User "+ (i+1)+ ":"+userName);
        	//store first userName
        	if(i==0) {
        		secondAprovalUser=userName;
        	}
        }
        System.out.println("Second User Name is: "+secondAprovalUser);
        
        
        //Verification of State after 1st Approval Approve
        test.info("Verification of State after 1st Approval Approve");
    	String state3=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Authorize']")).getText();
    	System.out.println("State is:"+state3);
        compareTwoStringsEquals("Authorize", state3);
        test.pass("Second Approval is generated");
        
      //Custom report
        Reporter.getCurrentTestResult().setAttribute("TestData", secondAprovalUser);
    }
    
    @Test(description = "Verification of 2nd Approval", dependsOnMethods = "OPNCHNAFTAPPL")
    public void impersonateUserSec() throws InterruptedException {
    	
    	jse = (JavascriptExecutor) driver;
    	
    	//End Impersonation
    	Thread.sleep(2000);
    	impersonation.endImpersonation(jse);
    	Thread.sleep(2000);
    	
    	test = ExtentReportManager.createTest("Verification of Impersonation and End Impersonation");
    	test.info("Impersonation for 2nd Approval");
    	impersonation.startImpersonation(secondAprovalUser, jse);
    	Thread.sleep(10000);
    	
    	Thread.sleep(2000);
        driver.get(baseUrl + "/sysapproval_approver_list");
        Thread.sleep(2000);
        
        //filter by change no
        test.info(" Search Change record in Approval table");
        WebElement Approvals=driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
        Approvals.click();
        Thread.sleep(3000);
        
        Select selectValu=new Select(Approvals);
        selectValu.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox2.sendKeys(changeNo);
        Thread.sleep(2000);
        globalSearchBox2.sendKeys(Keys.ENTER);
        
        //filter by approval state
//        test.info(" Search Change record in Approval table");
//        WebElement Approvals1=driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
//        Approvals1.click();
//        Thread.sleep(3000);
//        
//        Select selectValu1=new Select(Approvals1);
//        selectValu1.selectByVisibleText("Approver");
//        
//        Thread.sleep(2000);
//        WebElement globalSearchBox3 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
//        globalSearchBox3.sendKeys(secondAprovalUser);
//        Thread.sleep(2000);
//        globalSearchBox3.sendKeys(Keys.ENTER);
        
        WebElement approversearch=driver.findElement(By.xpath("//*[@id='sysapproval_approver_table']/thead/tr[2]/td[4]/div/div/div/input"));
       	approversearch.sendKeys(secondAprovalUser);
       	approversearch.sendKeys(Keys.ENTER);
       
       	test.info(" Opening Approval Change record");
       	WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
       	requestedbutton.click();
    
       	driver.findElement(By.xpath("//*[@id='approve']")).click();
       	Thread.sleep(2000);
    	
    	test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
    	
    	//Impersonating adimn user
    	Object[][] users = DataImport.getData("ImpersonateUser");
    	impersonation.startImpersonation(users[0][0].toString(), jse);
    	Thread.sleep(2000);
    
    }
    @Test(description = "Verification of State and Impliment UI Action after 2nd Approval Approved", dependsOnMethods = "impersonateUserSec")
    public void implimentUIAction() throws InterruptedException 
    {
    	test = ExtentReportManager.createTest("Verification of Change record after 2nd Approval Approved");
    	test.info("Change record opening after 2nd Approval");
    	
    	System.err.println("2nd Approval flow completed");
        Thread.sleep(2000);
        //Opening Change record After 2nd Approval Approve
        driver.get(baseUrl + "/change_request_list");
        Thread.sleep(2000);
        WebElement globalSearchBox4 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox4.sendKeys(changeNo);
        Thread.sleep(1000);
        globalSearchBox4.sendKeys(Keys.ENTER);
        
        Thread.sleep(2000);          
                        
        List<WebElement> openChange4 = driver.findElements(By.xpath("//table[@id='change_request_table']/tbody/tr/td[3]"));
        for (WebElement chan4 : openChange4) 
        {
            String currentChan4 = chan4.getText();
            if (currentChan4.contains(changeNo)) 
            {
          	  chan4.click();
                break;
            }
        }
        System.err.println("Opening the Change record after 2nd Approval");
        //Verification of State after 2nd Approval
        test.info("Verification of State after 2nd Approval");
    	String state4=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Scheduled']")).getText();
    	System.out.println("State is:"+state4);
        compareTwoStringsEquals("Scheduled", state4);
        test.info("Impliment UI Action");
        
        //Click on Impliment UI Action
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='state_model_move_to_implement']")).click();    
        
        test.info("State verification after Click on Impliment UI Action");
        //Verification of State after Click on Impliment UI Action
        test.info("Verification of State after Click on Impliment UI Action");
    	String state5=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Implement']")).getText();
    	System.out.println("State is:"+state5);
    	compareTwoStringsEquals("Implement", state5);
    }
    
    @Test(description = "Verification of 1st Change Task", dependsOnMethods = "implimentUIAction")
    public void firstchangeTask() throws InterruptedException 
    {
    	test = ExtentReportManager.createTest("Verification of Change Task records ");
    	test.info("Opening 1st Change Task record");
    	Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id='tabs2_list']/span[4]/span")).click();  
        System.out.println("Opening 1st Change Task record"); 
        Actions action= new Actions(driver);
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
        //Opening 1st Change Task record
        By changeTaskLocator = By.xpath("(//*[contains(@id,'row_change_request.change_task.change_request')]/td[3]/a)[1]");
        driver.findElement(changeTaskLocator).click();
        
        test.info("State verification");
        //Verification of State
        String scTState1=driver.findElement(By.xpath("//*[@id='change_task.state']/option[text()='Open']")).getText();
        System.out.println("State is :"+scTState1);
        compareTwoStringsEquals("Open", scTState1);
        
     // Copy Change Task record number
        test.info("Copy Change Task record number");
        WebElement number = driver.findElement(By.xpath("//input[@id='change_task.number']"));
          String copyNumber = number.getAttribute("value");
          System.out.println("Change Task number:"+ copyNumber);
          Thread.sleep(5000);
        
        Thread.sleep(5000);
      //Click on Close Task UI Action
        test.info("Verification of Close Task UI Action");
        WebElement closeTAsk=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
        closeTAsk.click();
        
      //Error massage validation and print
        test.info("Error massage becouse mandatory fields are empty");
        String errorMassage2=driver.findElement(By.xpath("//*[@class='outputmsg_text']")).getText();
        System.out.println("Error Massage is:"+errorMassage2);
        compareTwoStringsEquals("The following mandatory fields are not filled in: Close code, Close notes", errorMassage2);

        test.info("Verification of Assignment group field");
        //Verification of assignment_group field
        WebElement assiGroup=driver.findElement(By.xpath("//*[@id='sys_display.change_task.assignment_group']"));
        System.out.println("element is present :"+assiGroup.isDisplayed());
        action.moveToElement(assiGroup).click().build().perform();
        assiGroup.sendKeys("CAB Approval" + Keys.ENTER);
        
        //Verification of assigned to field
        test.info("Verification of Assigned to field");
        WebElement assignedto=driver.findElement(By.xpath("//*[contains(@id,'sys_display.change_task.assigned_to')]"));
        action.moveToElement(assignedto).click().build().perform();
        assignedto.sendKeys("Bernard Laboy" + Keys.ENTER);
        
        test.info("Closure Information Tab");
       //Click on Closure Information Tab
       Thread.sleep(5000);
       WebElement ClosureInformation2=driver.findElement(By.xpath("//*[@id='tabs2_section']/span[2]/span[1]/span[2]"));
       ClosureInformation2.click();
       
       //Verification of Closed Code field
       test.info("Verification of Closed Code field");
       Thread.sleep(3000);
       System.out.println("Select option to Closed Code field");
       WebElement closeCode=driver.findElement(By.xpath("//select[@id='change_task.close_code']"));
       closeCode.isDisplayed();
       closeCode.click();
       Select closeCodefield=new Select(closeCode);
       Thread.sleep(3000);
       closeCodefield.selectByVisibleText("Successful");
       
       //Verification of Closed Note field
       test.info("Verification of Closed Note field");
       Thread.sleep(3000);
       System.out.println("Select Value to Closed Note field");
       WebElement closeNotes=driver.findElement(By.xpath("//*[@id='change_task.close_notes']"));
       closeNotes.sendKeys("Value for change Task record");
       
       test.pass("1st change Task flow is completed");
     //Click on Close Task UI Action
       Thread.sleep(3000);
       closeTAsk.click();
       Thread.sleep(3000);
    }
    
    
    @Test(description = "Verification of 2nd Change Task", dependsOnMethods = "implimentUIAction")
    public void secondchangeTask() throws InterruptedException 
    {
    	//Click on Change Task tab
        Thread.sleep(2000);
        test = ExtentReportManager.createTest("Verification of Change Task records ");
    	test.info("Opening 2nd Change Task record");
        System.out.println("Opening 2nd Change Task record"); 
      //Opening 2nd Change Task record
        Actions action2= new Actions(driver);
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(20));
        By changeTaskLocator2 = By.xpath("(//*[contains(@id,'row_change_request.change_task.change_request')]/td[3]/a)[2]");
        Thread.sleep(2000);
        driver.findElement(changeTaskLocator2).click();

      //Verification of State
        test.info("State verification");
        String scTState3=driver.findElement(By.xpath("//*[@id='change_task.state']/option[text()='Open']")).getText();
        System.out.println("State is :"+scTState3);
        compareTwoStringsEquals("Open", scTState3);
        
        // Copy Change record number
        test.pass("Change Task Number");
        WebElement number2 = driver.findElement(By.xpath("//input[@id='change_task.number']"));
          String copyNumber2 = number2.getAttribute("value");
          System.out.println("Change Task number:"+ copyNumber2);
          Thread.sleep(5000);
          
        //Click on Close Task UI Action
          Thread.sleep(5000);
          test.info("Verification of Close Task UI Action");
            WebElement closeTAsk2=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
            closeTAsk2.click();
            Thread.sleep(5000);
            
          //Error massage validation and print
            test.info("Verification of Error massage when mandatory fields are Empty");
            String errorMassage3=driver.findElement(By.xpath("//*[@class='outputmsg_text']")).getText();
            System.out.println("Error Massage is:"+errorMassage3);
            compareTwoStringsEquals("The following mandatory fields are not filled in: Close code, Close notes", errorMassage3);
            
          //Verification of assignment_group field
            test.info("Verification of Assignment group field");
            Thread.sleep(5000);
            WebElement assiGroup2=driver.findElement(By.xpath("//*[@id='sys_display.change_task.assignment_group']"));
            System.out.println("element is present :"+assiGroup2.isDisplayed());
            Thread.sleep(3000);
            action2.moveToElement(assiGroup2).click().build().perform();
            assiGroup2.sendKeys("CAB Approval" + Keys.ENTER);
            
            //Verification of assigned to field
            test.info("Verification of assigned to field");
            WebElement assignedto2=driver.findElement(By.xpath("//*[contains(@id,'sys_display.change_task.assigned_to')]"));
            action2.moveToElement(assignedto2).click().build().perform();
            assignedto2.sendKeys("Bernard Laboy" + Keys.ENTER);
            
              //Click on Closure Information tab
            test.info("Verification of fields under Information tab");
              WebElement ClosureInformation3=driver.findElement(By.xpath("//*[@id='tabs2_section']/span[2]/span[1]/span[2]"));
              ClosureInformation3.click();
            
              Thread.sleep(3000);
              test.info("Verification of Closed Code field");
              System.out.println("Select option to Closed Code field");
              WebElement closeCode3=driver.findElement(By.xpath("//select[@id='change_task.close_code']"));
              Thread.sleep(3000);
              closeCode3.isDisplayed();
              Thread.sleep(3000);
              closeCode3.click();  
              Select closeCodefield3=new Select(closeCode3);
              Thread.sleep(3000);
              closeCodefield3.selectByVisibleText("Successful");
              Thread.sleep(3000);
              
              
              //Verification of Close Note field
              test.info("Verification of Close Note field");
              System.out.println("Select Value to Closed Note field");
              WebElement closeNotes3=driver.findElement(By.xpath("//*[@id='change_task.close_notes']"));
              Thread.sleep(5000);
              closeNotes3.sendKeys("Value for change Task record");
              
            //Click on Close Task UI Action
              test.pass("2nd change Task flow is completed");
              Thread.sleep(5000);
              WebElement closeTAsk1=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
              closeTAsk1.click();
              Thread.sleep(3000);
    }
    
    @Test(description = "Verification of Change Record", dependsOnMethods = "secondchangeTask")
    public void Closechangerecord() throws InterruptedException 
    {
    	Thread.sleep(2000);
    	test = ExtentReportManager.createTest("Verification of Change records after Tasks are closed");
    	
    	test.info("State verification");
        //Verification of State
        Thread.sleep(2000);
        String chnState5=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Review']")).getText();
        System.out.println("State is :"+chnState5);
        compareTwoStringsEquals("Review", chnState5);
        Thread.sleep(3000);
    	
        //Click on Closed Button without fill mandatory fields 
    	test.info("Verification of Error massage on Change record when mandatory fields are Empty");
    	Thread.sleep(5000);
        WebElement closebutton = driver.findElement(By.xpath("//*[@id='state_model_move_to_closed']"));
        Thread.sleep(2000);
        closebutton.isDisplayed();
        Thread.sleep(2000);
        closebutton.click();
        Thread.sleep(2000);
        
        //Error massage validation and print
        String errorMasPri= driver.findElement(By.xpath("//*[@id='output_messages']")).getText();
        System.out.println("Error massage is:"+errorMasPri);
        Thread.sleep(2000); 
         
        Thread.sleep(3000);
        System.out.println("Select option to Closed Code field");
        //Verification of Closed Code field
        WebElement closeCode4=driver.findElement(By.xpath("//select[@id='change_request.close_code']"));
        Thread.sleep(3000);
        closeCode4.isDisplayed();
        closeCode4.click();
        Select closeCodefield4=new Select(closeCode4);
        Thread.sleep(3000);
        closeCodefield4.selectByVisibleText("Successful");
        
        Thread.sleep(3000);
        System.out.println("Select Value to Closed Note field");
      //Verification of Closed Note field
        WebElement closeNotes4=driver.findElement(By.xpath("//*[@id='change_request.close_notes']"));
        Thread.sleep(3000);
        closeNotes4.sendKeys("Value for change Task record");
        
        //Click on Close Task UI Action
        Thread.sleep(3000);
        test.pass("Change record flow is completed");
        WebElement closeTAsk4=driver.findElement(By.xpath("//*[@id='state_model_move_to_closed']"));
        closeTAsk4.click();
        
        test.info("State verification");
        //Verification of State
        Thread.sleep(3000);
        String chnState6=driver.findElement(By.xpath("//*[@id='change_request.state']/option[text()='Closed']")).getText();
        System.out.println("State is :"+chnState6);
        compareTwoStringsEquals("Closed", chnState6);
        Thread.sleep(5000);
        
     Thread.sleep(5000);
    }
}