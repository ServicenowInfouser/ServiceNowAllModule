
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
import utils.DataImport;
import utils.ExtentReportManager;


public class NormalChange extends BaseTest {
	private WebDriver driver = DriverManager.getDriver(); 
	String changeNo;
	private JavascriptExecutor jse;
		
	//Object[][] changedata = DataImport.getData("Change");
	
	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);
    
    @Test(description = "Verification of Navigate to Change list")
    public void navigateToChangeList() throws InterruptedException {
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
        WebElement models=driver.findElement(By.xpath("//*[@id=\"change_models\"]"));
        models.click();
        
        test.info("Clicking on the Normal change widget");
        Thread.sleep(5000);
        //Click on Normal widget
        WebElement NormalChange=driver.findElement(By.xpath("//*[@id=\"007c4001c343101035ae3f52c1d3aeb2\"]/div[1]/div[1]/span"));
        NormalChange.click();
        test.pass("Navigated to the New change page");
    }
    
    @Test(description = "Verification of Creation of change", dependsOnMethods = "navigateToChangeList")
    public void createChange() throws InterruptedException {
    	test = ExtentReportManager.createTest("Verification of Navigating to Change list view");
    	// Copy Change record number
        WebElement inputElement = driver.findElement(By.xpath("//input[@id='change_request.number']"));
        changeNo = inputElement.getAttribute("value");
        System.out.println("Change Number is:"+changeNo);
        
        test.info("Verification on fields on change record");
        // Set Group to Assignment group field
        WebElement Assignmentgroup = driver.findElement(By.xpath("//*[@id=\"sys_display.change_request.assignment_group\"]"));
        Assignmentgroup.sendKeys("Application Development" + Keys.ENTER);
        
        Thread.sleep(2000);
        // Set user to Assign to field
        WebElement Assignto = driver.findElement(By.xpath("//*[@id=\"sys_display.change_request.assigned_to\"]"));
        Assignto.sendKeys("Arya Hajarha" + Keys.ENTER);
        
        Thread.sleep(2000);
        // Enter short description
        driver.findElement(By.xpath("//*[@id=\"change_request.short_description\"]")).sendKeys("Create NormalChannge");

        test.info("verification of Submit UI Action");
        // Click on submit
        driver.findElement(By.xpath("//*[@id=\"sysverb_insert\"]")).click();
        
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
    public void requestingApproval() throws InterruptedException {
    	Thread.sleep(10000);
    	
    	test = ExtentReportManager.createTest("Verification of Request Approval UI action");
    	//Click on RequestApproval UI action 
    	test.info("Click on the Request Approval UI action");
        WebElement RequestApproval=driver.findElement(By.xpath("//*[@id='state_model_request_assess_approval']"));
        RequestApproval.click();

        //Click on the Approvers tab
        test.info("Go to the Approval Related list");
    	WebElement ApproversTab=driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[3]/span/span[2]"));
        ApproversTab.click();
        
        //Approval user
        List<WebElement> Approvers=driver.findElements(By.xpath("//*[@id=\"change_request.sysapproval_approver.sysapproval_table\"]/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println(Approvers.size());
        for (WebElement Users : Approvers) {
        	test.info("Approver users list" + Users.getText());
        	System.out.println(Users.getText());
        	Reporter.getCurrentTestResult().setAttribute("TestData", Users.getText());
       }
       test.pass("Approvals generated successfully");
    }
    
    
    @Test(description = "Verification of User Impersonation", dependsOnMethods = "requestingApproval")
    public void impersonateUser() throws InterruptedException {
    	
    	jse = (JavascriptExecutor) driver;
    	
    	test = ExtentReportManager.createTest("Verification of Approving the approval by Impersonating user");
    	test.info("Impersonation for first Approval");
    	//impersonation = new Impersonation(driver);
    	impersonation.startImpersonation("Manifah Masood", jse);

    	Thread.sleep(2000);
        driver.get(baseUrl + "/sysapproval_approver_list");
        Thread.sleep(2000);
        
        test.info(" Search Change record in Approval table");
        WebElement Approvals=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
        Approvals.click();
        Thread.sleep(3000);
        
        Select selectValu=new Select(Approvals);
        selectValu.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox2.sendKeys(changeNo);
        Thread.sleep(2000);
        globalSearchBox2.sendKeys(Keys.ENTER);
        
        WebElement approversearch=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
        approversearch.sendKeys("Manifah Masood");
        approversearch.sendKeys(Keys.ENTER);
       
        test.info("Opening Approval Change record");
        WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
        requestedbutton.click();
    
        test.info("Appriving the Approval");
        driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
        Thread.sleep(2000);
    	
    	test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
    }
    
    @Test(description = "Opening CHN record after first Approval Approve", dependsOnMethods = "impersonateUser")
    public void OPNCHNAFTAPPL() throws InterruptedException {
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
        List<WebElement> Approvers2=driver.findElements(By.xpath("//*[@id=\"change_request.sysapproval_approver.sysapproval_table\"]/tbody/tr/td[4]"));
        Thread.sleep(2000);
        System.out.println("Count of Approvers users are:"+Approvers2.size());
        for (WebElement Users2 : Approvers2) 
        {
            String ApproverUser2 = Users2.getText();
            System.out.println("List of Approver users are:"+Users2.getText());
        }
        
        //Verification of State after 1st Approval Approve
        WebElement State1stApp=driver.findElement(By.xpath("//*[@id=\"change_request.state\"]"));
        String stateafield3=State1stApp.getAttribute("value");
        System.out.println(State1stApp.getTagName());
        
        test.pass("Second Approval is generated");
    }
    
    @Test(description = "Verification of 2nd Approval", dependsOnMethods = "OPNCHNAFTAPPL")
    public void impersonateUserSec() throws InterruptedException {
    	
    	jse = (JavascriptExecutor) driver;
    	
    	test = ExtentReportManager.createTest("Verification of Impersonation and End Impersonation");
    	test.info("Impersonation for 2nd Approval");
    	impersonation.startImpersonation("Ron Kettering", jse);
    	Thread.sleep(10000);
    	
    	Thread.sleep(2000);
        driver.get(baseUrl + "/sysapproval_approver_list");
        Thread.sleep(2000);
        
        test.info(" Search Change record in Approval table");
        WebElement Approvals=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
        Approvals.click();
        Thread.sleep(3000);
        
        Select selectValu=new Select(Approvals);
        selectValu.selectByVisibleText("Approval for");
        
        Thread.sleep(2000);
        WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
        globalSearchBox2.sendKeys(changeNo);
        Thread.sleep(2000);
        globalSearchBox2.sendKeys(Keys.ENTER);
        
        WebElement approversearch=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
       	approversearch.sendKeys("Ron Kettering");
       	approversearch.sendKeys(Keys.ENTER);
       
       	test.info(" Opening Approval Change record");
       	WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
       	requestedbutton.click();
    
       	driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
       	Thread.sleep(2000);
    	
    	test.info("End Impersonation");
    	impersonation.endImpersonation(jse);
    	test.pass("Successfully approved the approval");
    
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
        WebElement verificationOfState=driver.findElement(By.xpath("//*[@id=\"change_request.state\"]"));
        String stateafter2ndApprovalApprove=verificationOfState.getAttribute("value");
        System.out.println(stateafter2ndApprovalApprove);

        test.info("Impliment UI Action");
        //Click on Impliment UI Action
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"state_model_move_to_implement\"]")).click();    
        
        test.info("State verification after Click on Impliment UI Action");
        //Verification of State after Click on Impliment UI Action
        WebElement StateAfterImplimentButton=driver.findElement(By.xpath("//*[@id=\"change_request.state\"]"));
        String stateafterclickonONImplimentUIAction=StateAfterImplimentButton.getAttribute("value");
        System.out.println(stateafterclickonONImplimentUIAction);
    }
    
    @Test(description = "Verification of 1st Change Task", dependsOnMethods = "implimentUIAction")
    public void firstchangeTask() throws InterruptedException 
    {
    	test = ExtentReportManager.createTest("Verification of Change Task records ");
    	test.info("Opening 1st Change Task record");
    	
    	Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"tabs2_list\"]/span[4]/span")).click();  
        System.out.println("Opening 1st Change Task record"); 
        
        Actions action= new Actions(driver);
        
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));

        By changeTaskLocator = By.xpath("(//*[contains(@id,'row_change_request.change_task.change_request')]/td[3]/a)[1]");

        driver.findElement(changeTaskLocator).click();
        
        test.info("State verification");
        //Verification of State
        WebElement veriStaCT=driver.findElement(By.xpath("//select[@id=\"change_task.state\"]"));
        System.out.println("State is :"+veriStaCT.getText());
        
     // Copy Change Task record number
        WebElement number = driver.findElement(By.xpath("//input[@id='change_task.number']"));
          String copyNumber = number.getAttribute("value");
          System.out.println("Change Task number:"+ copyNumber);
          Thread.sleep(5000);
        
        Thread.sleep(5000);
      //Click on Close Task UI Action
        WebElement closeTAsk=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
        closeTAsk.click();
        
        test.info("Error massage becouse mandatory fields are empty");
        WebElement errorMassage2=driver.findElement(By.xpath("//*[@class='outputmsg_text']"));
        System.out.println("Error Massage is:"+errorMassage2);

        test.info("Verification of assignment_group and assigned to fields");
        //Verification of assignment_group field
        WebElement assiGroup=driver.findElement(By.xpath("//*[@id='sys_display.change_task.assignment_group']"));
        System.out.println("element is present :"+assiGroup.isDisplayed());
       
        action.moveToElement(assiGroup).click().build().perform();
        assiGroup.sendKeys("CAB Approval" + Keys.ENTER);
        
        //Verification of assigned to field
        WebElement assignedto=driver.findElement(By.xpath("//*[contains(@id,'sys_display.change_task.assigned_to')]"));
        
        action.moveToElement(assignedto).click().build().perform();
        assignedto.sendKeys("Bernard Laboy" + Keys.ENTER);
        
        test.info("Closure Information Tab");
       //Click on Closure Information Tab
       Thread.sleep(5000);
       WebElement ClosureInformation2=driver.findElement(By.xpath("//*[@id=\"tabs2_section\"]/span[2]/span[1]/span[2]"));
       ClosureInformation2.click();
       
       test.info("Verification of Closed Code field");
       Thread.sleep(3000);
       System.out.println("Select option to Closed Code field");
       WebElement closeCode=driver.findElement(By.xpath("//select[@id='change_task.close_code']"));
       closeCode.isDisplayed();
       closeCode.click();
       Select closeCodefield=new Select(closeCode);
       Thread.sleep(3000);
       closeCodefield.selectByVisibleText("Successful");
       
       test.info("Verification of Closed Note field");
       Thread.sleep(3000);
       System.out.println("Select Value to Closed Note field");
       WebElement closeNotes=driver.findElement(By.xpath("//*[@id='change_task.close_notes']"));
       closeNotes.sendKeys("Value for change Task record");
       
       test.pass("1st change Task flow is completed");
     //Click on Close Task UI Action
       Thread.sleep(3000);
       closeTAsk.click();
       
       Thread.sleep(5000);
    }
    
    
    @Test(description = "Verification of 2nd Change Task", dependsOnMethods = "implimentUIAction")
    public void secondchangeTask() throws InterruptedException 
    {
    	//Click on Change Task tab
        Thread.sleep(2000);
        test = ExtentReportManager.createTest("Verification of Change Task records ");
    	test.info("Opening 2nd Change Task record");
        System.out.println("Opening 2nd Change Task record"); 
        
        Actions action2= new Actions(driver);
        
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(20));

        By changeTaskLocator2 = By.xpath("(//*[contains(@id,'row_change_request.change_task.change_request')]/td[3]/a)[2]");

        driver.findElement(changeTaskLocator2).click();

        //Verification of State
        test.info("Verification of State");
        WebElement veriStaCT2=driver.findElement(By.xpath("//select[@id=\"change_task.state\"]"));
        System.out.println("State is :"+veriStaCT2.getText());
        
        // Copy Change record number
        test.pass("Change Task Number");
        WebElement number2 = driver.findElement(By.xpath("//input[@id='change_task.number']"));
          String copyNumber2 = number2.getAttribute("value");
          System.out.println("Change Task number:"+ copyNumber2);
          Thread.sleep(5000);
          
          Thread.sleep(5000);
          test.info("Verification of Close Task UI Action");
          //Click on Close Task UI Action
            WebElement closeTAsk2=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
            closeTAsk2.click();
            Thread.sleep(5000);
            
            test.info("Verification of Error massage when mandatory fields are Empty");
            WebElement errorMassage3=driver.findElement(By.xpath("//*[@class='outputmsg_text']"));
            System.out.println("Error Massage is:"+errorMassage3);
            
          //Verification of assignment_group field
            test.info("Verification of fields");
            Thread.sleep(5000);
            WebElement assiGroup2=driver.findElement(By.xpath("//*[@id='sys_display.change_task.assignment_group']"));
            System.out.println("element is present :"+assiGroup2.isDisplayed());
           
            Thread.sleep(3000);
            action2.moveToElement(assiGroup2).click().build().perform();
            assiGroup2.sendKeys("CAB Approval" + Keys.ENTER);
            
            //Verification of assigned to field
            WebElement assignedto2=driver.findElement(By.xpath("//*[contains(@id,'sys_display.change_task.assigned_to')]"));
            
            action2.moveToElement(assignedto2).click().build().perform();
            assignedto2.sendKeys("Bernard Laboy" + Keys.ENTER);
            
              //Click on Closure Information tab
            test.info("Verification of fields under Information tab");
              WebElement ClosureInformation3=driver.findElement(By.xpath("//*[@id=\"tabs2_section\"]/span[2]/span[1]/span[2]"));
              ClosureInformation3.click();
            
              Thread.sleep(5000);
              System.out.println("Select option to Closed Code field");
              WebElement closeCode3=driver.findElement(By.xpath("//select[@id='change_task.close_code']"));
              Thread.sleep(3000);
              closeCode3.isDisplayed();
              closeCode3.click();
              
              Select closeCodefield3=new Select(closeCode3);
              Thread.sleep(5000);
              closeCodefield3.selectByVisibleText("Successful");
              
              Thread.sleep(5000);
              System.out.println("Select Value to Closed Note field");
              WebElement closeNotes3=driver.findElement(By.xpath("//*[@id='change_task.close_notes']"));
              Thread.sleep(5000);
              closeNotes3.sendKeys("Value for change Task record");
              
            //Click on Close Task UI Action
              test.pass("2nd change Task flow is completed");
              Thread.sleep(5000);
              WebElement closeTAsk1=driver.findElement(By.xpath("//*[@id='change_task_to_closed']"));
              closeTAsk1.click();
    
    }
    
    @Test(description = "Verification of 2nd Change Task", dependsOnMethods = "implimentUIAction")
    public void Closechangerecord() throws InterruptedException 
    {
    	Thread.sleep(2000);
    	test = ExtentReportManager.createTest("Verification of Change records after Tasks are closed");
    	test.info("Verification of Error massage on Change record when mandatory fields are Empty");
        WebElement closebutton = driver.findElement(By.xpath("//*[@id='state_model_move_to_closed']"));
        closebutton.isDisplayed();
        closebutton.click();
        Thread.sleep(1000);
        
        //Error massage validation and print
        WebElement errorMasPri= driver.findElement(By.xpath("//*[@id=\"output_messages\"]"));
        System.out.println("Error massage is:"+errorMasPri);
        Thread.sleep(2000); 
         
        Thread.sleep(5000);
        System.out.println("Select option to Closed Code field");
        //Verification of Closed Code field
        WebElement closeCode4=driver.findElement(By.xpath("//select[@id='change_request.close_code']"));
        Thread.sleep(3000);
        closeCode4.isDisplayed();
        closeCode4.click();
        
        Select closeCodefield4=new Select(closeCode4);
        Thread.sleep(5000);
        closeCodefield4.selectByVisibleText("Successful");
        
        Thread.sleep(5000);
        System.out.println("Select Value to Closed Note field");
      //Verification of Closed Note field
        WebElement closeNotes4=driver.findElement(By.xpath("//*[@id='change_request.close_notes']"));
        Thread.sleep(5000);
        closeNotes4.sendKeys("Value for change Task record");
        
        //Click on Close Task UI Action
        Thread.sleep(5000);
        test.pass("Change record flow is completed");
        WebElement closeTAsk4=driver.findElement(By.xpath("//*[@id='state_model_move_to_closed']"));
        closeTAsk4.click();
        
        WebElement stateofChange=driver.findElement(By.xpath("//*[@id='change_request.state']"));
        String printstate=stateofChange.getAttribute("option");
        System.out.println("State is:"+printstate);
        
        
     
     Thread.sleep(5000);
    }
}