package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Navigator extends BaseTest {
	
	private WebDriver driver;

    public Navigator(WebDriver driver) {
        this.driver = driver;
    }
	
	//All menu Navigation and opening given list
    public void allNavigation(String listName, JavascriptExecutor jse) throws InterruptedException {
    	//test.info("Navigating to All Menu");
    	String all="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";
		WebElement allclick=(WebElement)jse.executeScript(all);
		allclick.click();
		
		//test.info("Search and open list");
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
    
    public void newUIAction(JavascriptExecutor jse) throws InterruptedException {
    	String newbutton="return document.querySelector(\"#sysverb_new\")";
		WebElement clicknewui =(WebElement) jse.executeScript(newbutton);
		clicknewui.click();
    }
}