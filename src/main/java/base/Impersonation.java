package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.WaitUtils;

public class Impersonation extends BaseTest {
    
    private WebDriver driver;
    private WaitUtils waitUtils;
    
    // Selectors
    String profileIconJS = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span\")";
    String impJS = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div\")";
    String searchUserJS = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\")";
    String clickUserJS = "return document.querySelector(\"body > now-popover-panel > seismic-hoist\")";
    String clickIMPbuttonJS = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button > slot > span\")";
    String endImpJS = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.unimpersonate.keyboard-navigatable.polaris-enabled > div\")";

    public Impersonation(WebDriver driver) {
    	this.driver = driver;
        this.waitUtils = new WaitUtils(driver); // Initialize the utility
    }

    /**
     * Reusable Fluent Wait helper for Shadow DOM elements via JavaScript
     */
//    private WebElement waitForElement(String jsScript) {
//        Wait<WebDriver> wait = new FluentWait<>(driver)
//                .withTimeout(Duration.ofSeconds(20))
//                .pollingEvery(Duration.ofMillis(500))
//                .ignoring(Exception.class);
//
//        return wait.until(d -> {
//            WebElement element = (WebElement) ((JavascriptExecutor) d).executeScript(jsScript);
//            return (element != null && element.isDisplayed()) ? element : null;
//        });
//    }

    public void startImpersonation(String user, JavascriptExecutor jse) {
        try {
            driver.get(Config.baseUrl());
            
            // 1. Click Profile Icon
            waitUtils.waitForShadowElement(profileIconJS).click();

            // 2. Click Impersonation Button
            waitUtils.waitForShadowElement(impJS).click();

            // 3. Enter User Name
            WebElement searchField = waitUtils.waitForShadowElement(searchUserJS);
            searchField.sendKeys(user);
            Thread.sleep(2000);

            // 4. Select Searched User
            waitUtils.waitForShadowElement(clickUserJS).click();

            // 5. Click the final Impersonate button
            waitUtils.waitForShadowElement(clickIMPbuttonJS).click();
            
            System.out.println("Successfully impersonated: " + user);

        } catch (Exception e) {
            System.err.println("Failed to impersonate user: " + e.getMessage());
        }
    }

    public void endImpersonation(JavascriptExecutor jse) {
        try {
            driver.get(Config.baseUrl());
            
            // Click Profile Icon
            waitUtils.waitForShadowElement(profileIconJS).click();

            // Click End Impersonation
            waitUtils.waitForShadowElement(endImpJS).click();
            
            System.out.println("Impersonation ended.");
        } catch (Exception e) {
            System.err.println("Failed to end impersonation: " + e.getMessage());
        }
    }
}
//
//package base;
//
//import java.time.Duration;
//
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//public class Impersonation extends BaseTest {
//	
//	private WebDriver driver;
//	
//	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//	
//	String profileIcon="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span\")";
//
//    public Impersonation(WebDriver driver) {
//        this.driver = driver;
//    }
//	
//	public void startImpersonation(String user, JavascriptExecutor jse) throws InterruptedException {
//		try {
//			//Profile Icon click  
//			driver.get(Config.baseUrl());     
//			Thread.sleep(2000);
//			String profileIcon="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span\")";
//			WebElement profile=(WebElement)jse.executeScript(profileIcon);
//			profile.click();    
//			Thread.sleep(3000);
//			
//			//Impersonation Button click 
//			String imp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div\")";
//			WebElement profileImpersonation=(WebElement)jse.executeScript(imp);
//			profileImpersonation.click();
//			
//			//Entering User name in search field
//			Thread.sleep(3000);
//			String searchUser="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\")";
//			WebElement serchprofile=(WebElement)jse.executeScript(searchUser);
//			serchprofile.sendKeys(user);
//			
//			//Selecting searched user
//			Thread.sleep(3000);
//			String clickUser = "return document.querySelector(\"body > now-popover-panel > seismic-hoist\")";
//			//document.querySelector("body > now-popover-panel > seismic-hoist").shadowRoot.querySelector("#\\36 2826bf03710200044e0bfc8bcbe5df1")
//			//document.querySelector("body > now-popover-panel > seismic-hoist")
//			//String clickUser="return document.querySelector(\"body > now-popover-panel > seismic-hoist\").shadowRoot.querySelector(\"#\\\\31 832fbe1d701120035ae23c7ce610369\")";
//			WebElement clickUser1=(WebElement)jse.executeScript(clickUser);
//			clickUser1.click();
//			
//			//Clicking on the Impersonate button
//			Thread.sleep(3000);
//			String clickIMPbutton="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button > slot > span\")";
//			WebElement ipmButton=(WebElement)jse.executeScript(clickIMPbutton);
//			ipmButton.click();
//			System.out.println("Impersonated " + user);
//			
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//	
//	public void endImpersonation(JavascriptExecutor jse) throws InterruptedException {
//        try {
//			//End Impersonation
//			driver.get(Config.baseUrl()); 
//			Thread.sleep(3000);
//			WebElement profile=(WebElement)jse.executeScript(profileIcon);
//			profile.click();    
//			
//			Thread.sleep(3000);
//			String endimp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.unimpersonate.keyboard-navigatable.polaris-enabled > div\")";
//			WebElement endimpersonation=(WebElement)jse.executeScript(endimp);
//			endimpersonation.click();
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//}