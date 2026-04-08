package com.example.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import base.BaseTest;
import base.Config;
import base.DriverManager;
import utils.ExtentReportManager;

public class OrderGuide extends BaseTest {

	private WebDriver driver = DriverManager.getDriver();
	private JavascriptExecutor jse;

	Actions actions = new Actions(driver);

	String reqNumber;

	@Test(description = "SC_001- Verification of Submitting the Order Guide")
	public void createOrderGuideRecord() throws InterruptedException {
		test1 = ExtentReportManager.createTest("------- Order Guide Flow Started -------");

		test = ExtentReportManager.createTest("SC_001- Verification of Submitting the Order Guide");

		// Navigating to the ESC Portal
		driver.get(Config.baseUrl()+"/esc");

		// Search New Hire Order Guide
		Thread.sleep(7000);
		WebElement searchbar = driver.findElement(By.xpath("//*[@id=\"homepage-search\"]/div/div/form/div/input"));
		searchbar.sendKeys("New Hire");

		actions.sendKeys(Keys.ENTER).perform();

		// Open Order Guide
		Thread.sleep(7000);
		WebElement orderguide = driver.findElement(By.xpath(
				"//*[@id=\"x0089509c732023009512e1e54cf6a7d3\"]/div/div/div/div[1]/div[2]/div[2]/div/div[3]/div/div[1]/div[1]/div/div/h4/a"));
		orderguide.click();

		// Filling the Order Guide
		Thread.sleep(7000);
		WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"s2id_sp_formfield_hiring_group\"]/a"));
		dropdown.click();
		Thread.sleep(5000);
		WebElement it = driver.findElement(By.xpath("//div[text()= \"IT\"]"));
		it.click();

		WebElement next = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
		next.click();

		Thread.sleep(5000);
		WebElement header = driver
				.findElement(By.xpath("//*[@id=\"accordion-header-186d917a6fab7980575967ddbb3ee4f2\"]"));
		header.click();

		Thread.sleep(5000);
		WebElement switch1 = driver.findElement(
				By.xpath("//*[@id=\"e1be6dcb4f7b4200086eeed18110c74c\"]/div[1]/span/div/div/div/div/div[2]/div/label"));
		switch1.click();

		Thread.sleep(5000);
		WebElement switch2 = driver.findElement(
				By.xpath("//*[@id=\"8b3ae7fedc1be1004ece5c08239e522b\"]/div[1]/span/div/div/div/div/div[2]/div/label"));
		switch2.click();

		Thread.sleep(5000);
		WebElement switch3 = driver.findElement(
				By.xpath("//*[@id=\"962967674ff38200086eeed18110c7e7\"]/div[1]/span/div/div/div/div/div[2]/div/label"));
		switch3.click();

		Thread.sleep(5000);
		WebElement email = driver.findElement(By.xpath("//*[@id=\"sp_formfield_new_email\"]"));
		email.sendKeys("ayush@test.com");

		Thread.sleep(5000);
		WebElement next2 = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
		next2.click();

		Thread.sleep(5000);
		WebElement submit = driver.findElement(By.xpath("//*[@id=\"submit-btn\"]"));
		submit.click();

		Thread.sleep(5000);
		WebElement checkout = driver.findElement(By.xpath("//*[@id=\"sc_cat_checkout\"]/div[3]/div[2]/div/button[2]"));
		checkout.click();

		Thread.sleep(5000);
		WebElement req = driver
				.findElement(By.xpath("//*[@id=\"x6fb0f4029f8332002528d4b4232e70f6\"]/div[2]/div[2]/div/div[2]/b"));
		reqNumber = req.getText();
		System.out.println("The Number is " + reqNumber);
	}

	@Test(description = "SC_002- Verification of Request and RITM", dependsOnMethods = "createOrderGuideRecord")
	public void checkingOrderGuideRecord() throws InterruptedException {
		test = ExtentReportManager.createTest("SC_002- Verification of Request and RITM");
		
		//Navigating to the Base URL
		driver.get(Config.baseUrl());
		jse = (JavascriptExecutor) driver;
		// Open Requested Item
		// click on All navigation
		String all = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";
		WebElement allclick = (WebElement) jse.executeScript(all);
		allclick.click();

		// send Request text in All search
		String request = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\").shadowRoot.querySelector(\"#filter\")";
		WebElement requesttype = (WebElement) jse.executeScript(request);
		requesttype.sendKeys("Request");
		Thread.sleep(9000);
		String req1 = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\").shadowRoot.querySelector(\"nav > div.sn-polaris-nav.d6e462a5c3533010cbd77096e940dd8c.can-animate > div.super-filter-container.all-results-open > div.all-results-section.section-open.results-section > div > div.sn-polaris-tab-content.-left.is-visible.can-animate > div > sn-collapsible-list:nth-child(1)\").shadowRoot.querySelector(\"#e661dff4c611227b01af0af70d4b67f1 > span > span > mark\")";
		WebElement request1 = (WebElement) jse.executeScript(req1);
		request1.click();

		// Handle Views
		String iframe = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"#gsft_main\")";
		WebElement iframe1 = (WebElement) jse.executeScript(iframe);
		driver.switchTo().frame(iframe1);

		String navbar = "return document.querySelector(\"#list_nav_task\")";
		WebElement navbar1 = (WebElement) jse.executeScript(navbar);
		actions.contextClick(navbar1).perform();

		Thread.sleep(7000);

		String view = "return document.querySelector(\"#context_list_titletask > div:nth-child(2)\")";
		WebElement defaultview = (WebElement) jse.executeScript(view);
		defaultview.click();

		String default1 = "return document.querySelector(\"#b169dd7f0a0a0bbb0062fb97a5ea1e1c_task > div:nth-child(3)\")";
		WebElement default2 = (WebElement) jse.executeScript(default1);
		default2.click();
		Thread.sleep(9000);

		// Click on Magnifying Glass icon
		String searchbar12 = "return document.querySelector(\"#hdr_task > th.col-control.list-decoration-table > div > button\")";
		WebElement search1 = (WebElement) jse.executeScript(searchbar12);
		search1.click();

		Thread.sleep(9000);

		// Searching the Request
		String request4 = "return document.querySelector(\"#task_table_header_search_control\")";
		WebElement request5 = (WebElement) jse.executeScript(request4);
		request5.sendKeys(reqNumber);

		Thread.sleep(9000);

		driver.findElement(By.xpath("//a[text()=\"REQ0010016\"]")).click();

	}
}
