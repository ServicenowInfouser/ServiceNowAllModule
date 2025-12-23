package com.example.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Navigator{

    //All menu Navigation and opening given list
    public static void allNavigation(String listName, WebDriver driver, JavascriptExecutor jse) throws InterruptedException {
    	String all="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";
		WebElement allclick=(WebElement)jse.executeScript(all);
		allclick.click();
		
		String filter="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\").shadowRoot.querySelector(\"#filter\")";
		WebElement filtertype=(WebElement)jse.executeScript(filter);
		filtertype.sendKeys(listName);
		filtertype.sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		
		String iframe="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"#gsft_main\")";
		WebElement frame=(WebElement) jse.executeScript(iframe);
		driver.switchTo().frame(frame);
		Thread.sleep(3000);
    }
    
    public static void newUIAction(WebDriver driver, JavascriptExecutor jse) throws InterruptedException {
    	String newbutton="return document.querySelector(\"#sysverb_new\")";
		WebElement clicknewui =(WebElement) jse.executeScript(newbutton);
		clicknewui.click();
    }
}