package com.example.tests;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import base.Impersonation;
import base.Navigator;
import junit.framework.Assert;
import utils.DataImport;
import utils.ExtentReportManager;

public class Knowledge extends BaseTest {
	private WebDriver driver = DriverManager.getDriver();
	String knowledgeNo;
	private JavascriptExecutor jse;
	String firstAprovalUser = null, secondAprovalUser = null;

	Object[][] Knowledgedata = DataImport.getData("Knowledge");

	private Navigator navigator = new Navigator(driver);
	private Impersonation impersonation = new Impersonation(driver);

	public static void compareTwoStringsEquals(String expected, String actual) {
		Assert.assertEquals(expected, actual);
	}

	@Test(description = "Verification of Navigate to Knowledge list")
	public void navigateToKnowledgeList() throws InterruptedException {
		Thread.sleep(5000);
		jse = (JavascriptExecutor) driver;
		test = ExtentReportManager.createTest("Verification of Navigate to Knowledge list");

		jse = (JavascriptExecutor) driver;
		// Navigation through all menu
		test.info("Open Knowledge list from All menu", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());

		driver.get(baseUrl + "/kb_knowledge_list");
		test.info("Clicking on the New UI action", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		// Click on the New UI action

		Thread.sleep(2000);
		String newbutton2 = "return document.querySelector(\"#sysverb_new\")";
		WebElement clicknewui2 = (WebElement) jse.executeScript(newbutton2);
		clicknewui2.click();

		Thread.sleep(2000);
		test.info("Clicking on the Standard Template option", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		// Click on Standard Template option
		WebElement template = driver.findElement(By.xpath("/html/body/form/div[2]/a"));
		template.isDisplayed();
		template.click();
	}

	@Test(description = "Verification of Fields on Knowledge Record", dependsOnMethods = "navigateToKnowledgeList")
	public void submitingKnowledge() throws InterruptedException {
		WebElement inputElement = driver.findElement(By.xpath("//input[@id='sys_readonly.kb_knowledge.number']"));
		knowledgeNo = inputElement.getAttribute("value");
		System.out.println("Knowledge Number is:" + knowledgeNo);

		test = ExtentReportManager.createTest("Verification of Fields on Knowledge Record");
		test.info("Verification of Submit button when mandatory fields are Empty", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		// Click on Submit button
		Thread.sleep(2000);
		WebElement submit = driver.findElement(By.xpath("//button[@id='sysverb_insert']"));
		submit.click();

		// Verification of Error massage when mandatory fields are Empty
		test.info("Verification of Error massage without fill mandatory fields", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		String errorMassage1 = driver.findElement(By.xpath("//*[@id='output_messages']/div/div/span[2]")).getText();
		System.out.println("Error massage is :" + errorMassage1);

		test.info("Fill Knowledgebase field");
		String Knowledgebase = Knowledgedata[0][0].toString();
		driver.findElement(By.xpath("//*[@id='sys_display.kb_knowledge.kb_knowledge_base']")).sendKeys(Knowledgebase);

		// Adding Short Description
		test.info("Fill Short Description field");
		String ShortDescription = Knowledgedata[0][1].toString();
		driver.findElement(By.xpath("//*[@id='kb_knowledge.short_description']")).sendKeys(ShortDescription);

		Thread.sleep(1000);
		// Click on Submit button
		test.info("Verification of Submit button", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		WebElement submit1 = driver.findElement(By.xpath("//button[@id='sysverb_insert']"));
		submit1.click();

		test.pass("Knowledge record Submited Successfully");
	}

	@Test(description = "Verification of opening Created Knowledge record from list", dependsOnMethods = "submitingKnowledge")
	public void publishState() throws InterruptedException {
		test = ExtentReportManager.createTest("Verification of opening Created Knowledge record from list");
		driver.get(Config.baseUrl() + "/kb_knowledge_list");
		Thread.sleep(2000);

		WebElement searchbox = driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
		searchbox.click();
		Thread.sleep(1000);
		Select selectValu = new Select(searchbox);
		selectValu.selectByVisibleText("Number");

		// Search Knowledge record on table
		test.info("Verification of Knowledge record on table", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox.sendKeys(knowledgeNo);
		Thread.sleep(1000);
		globalSearchBox.sendKeys(Keys.ENTER);
		Thread.sleep(2000);

		// Open Knowledge
		List<WebElement> openKNw = driver.findElements(By.xpath("//table[@id='kb_knowledge_table']/tbody/tr/td[3]/a"));
		for (WebElement ele2 : openKNw) {
			String currentKNw = ele2.getText();
			if (currentKNw.contains(knowledgeNo)) {
				ele2.click();
				break;
			}
		}

		// Verification of State
		Thread.sleep(1000);
		test.info("Verification of State");
		String state = driver
				.findElement(By.xpath("//*[@id='sys_readonly.kb_knowledge.workflow_state']/option[text()='Draft']"))
				.getText();
		System.out.println("State is:" + state);
		compareTwoStringsEquals("Draft", state);

		// Click on Published UI Action
		test.info("Verification of Published UI Action");
		WebElement Published = driver.findElement(By.xpath("//*[@id='publish_knowledge']"));
		Published.click();

		test.pass("Knowledge record Published", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Reporter.getCurrentTestResult().setAttribute("TestData", knowledgeNo);
	}

	@Test(description = "Verification of opening Created Knowledge record from list after Published State", dependsOnMethods = "publishState")
	public void ViewArticle() throws InterruptedException {
		test = ExtentReportManager
				.createTest("Verification of opening Created Knowledge record from list after Published State");
		driver.get(Config.baseUrl() + "/kb_knowledge_list");
		Thread.sleep(2000);

		WebElement searchbox = driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
		searchbox.click();
		Thread.sleep(1000);
		Select selectValu = new Select(searchbox);
		selectValu.selectByVisibleText("Number");

		// Search Knowledge record on table
		test.info("Verification of Knowledge record on table", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox.sendKeys(knowledgeNo);
		Thread.sleep(1000);
		globalSearchBox.sendKeys(Keys.ENTER);
		Thread.sleep(2000);

		// Open Knowledge
		List<WebElement> openKNw = driver.findElements(By.xpath("//table[@id='kb_knowledge_table']/tbody/tr/td[3]/a"));
		for (WebElement ele2 : openKNw) {
			String currentKNw = ele2.getText();
			if (currentKNw.contains(knowledgeNo)) {
				ele2.click();
				break;
			}
		}

		// Verification of State
		Thread.sleep(1000);
		test.info("Verification of State");
		String state = driver
				.findElement(By.xpath("//*[@id='sys_readonly.kb_knowledge.workflow_state']/option[text()='Published']"))
				.getText();
		System.out.println("State is:" + state);
		compareTwoStringsEquals("Published", state);

		// Click on View Article realted link
		test.info("Verification of View Article realted link");
		WebElement ViewArticle = driver.findElement(By.xpath("//*[@id='cb1ad7720a0a3c1901fdabc99f970748']"));
		ViewArticle.click();

		// Click on Yes option for Help full
		Thread.sleep(1000);
		test.info("Verification of Yes option for Help full");
		driver.findElement(By.xpath("//*[@class='btn btn-success-subdued kb-article-yes-btn']")).click();

		// Verification of Massage
		Thread.sleep(2000);
		test.info("Verification of Massage after click on Yes option for Help full", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		String Massage = driver.findElement(By.xpath("//*[@id='kb_feedback_notification']")).getText();
		System.out.println("Massage is :" + Massage);

		// Click on Edit UI Action
		test.info("Verification of Edit UI Action");
		driver.findElement(By.xpath("//*[@id='editArticle']")).click();

		// Click on Retire UI Action
		test.info("Verification of Retire UI Action");
		driver.findElement(By.xpath("//*[@id='retire_replacement']")).click();
		Thread.sleep(3000);

		String originalWindow = driver.getWindowHandle();
		Set<String> allWindows = driver.getWindowHandles();
		for (String windowHandle : allWindows) {
			if (!windowHandle.equals(originalWindow)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}
		System.out.println("New window title: " + driver.getTitle());
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id='retire-button']")).click();

		test.pass("Knowledge record Retire", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		Reporter.getCurrentTestResult().setAttribute("TestData", knowledgeNo);
	}

	@Test(description = "Verification of opening Knowledge record from list after Retired State", dependsOnMethods = "ViewArticle")
	public void retiredarticle() throws InterruptedException {
		test = ExtentReportManager.createTest("Verification of opening Knowledge record from list after Retired State");
		driver.get(Config.baseUrl() + "/kb_knowledge_list");
		Thread.sleep(2000);

		WebElement searchbox = driver.findElement(By.xpath("//select[@class='form-control default-focus-outline']"));
		searchbox.click();
		Thread.sleep(1000);
		Select selectValu = new Select(searchbox);
		selectValu.selectByVisibleText("Number");

		// Search Knowledge record on table
		test.info("Verification of Knowledge record on table", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		WebElement globalSearchBox = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox.sendKeys(knowledgeNo);
		Thread.sleep(1000);
		globalSearchBox.sendKeys(Keys.ENTER);
		Thread.sleep(2000);

		// Open Knowledge
		List<WebElement> openKNw = driver.findElements(By.xpath("//table[@id='kb_knowledge_table']/tbody/tr/td[3]/a"));
		for (WebElement ele2 : openKNw) {
			String currentKNw = ele2.getText();
			if (currentKNw.contains(knowledgeNo)) {
				ele2.click();
				break;
			}
		}

		// Verification of State
		Thread.sleep(1000);
		test.info("Verification of State", MediaEntityBuilder
				.createScreenCaptureFromPath(ExtentReportManager.captureScreenshot_new(driver)).build());
		String state = driver
				.findElement(By.xpath("//*[@id='sys_readonly.kb_knowledge.workflow_state']/option[text()='Retired']"))
				.getText();
		System.out.println("State is:" + state);
		compareTwoStringsEquals("Retired", state);
	}
}
