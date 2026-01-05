package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Impersonation extends BaseTest {
	
	private WebDriver driver;
	
	String profileIcon="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span\")";

    public Impersonation(WebDriver driver) {
        this.driver = driver;
    }
	
	public void startImpersonation(String user, JavascriptExecutor jse) throws InterruptedException {
		//Profile Icon click  
        driver.get(Config.baseUrl());     
        Thread.sleep(2000);
        WebElement profile=(WebElement)jse.executeScript(profileIcon);
        profile.click();    
        Thread.sleep(3000);
        //Impersonation Button click 
        String imp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div\")";
        WebElement profileImpersonation=(WebElement)jse.executeScript(imp);
        profileImpersonation.click();
        
        //Entering User name in search field
        Thread.sleep(3000);
        String searchUser="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\")";
        WebElement serchprofile=(WebElement)jse.executeScript(searchUser);
        serchprofile.sendKeys(user);
        
        //Selecting searched user
        Thread.sleep(3000);
        String clickUser = "return document.querySelector(\"body > now-popover-panel > seismic-hoist\")";
        //document.querySelector("body > now-popover-panel > seismic-hoist").shadowRoot.querySelector("#\\36 2826bf03710200044e0bfc8bcbe5df1")
        //document.querySelector("body > now-popover-panel > seismic-hoist")
        //String clickUser="return document.querySelector(\"body > now-popover-panel > seismic-hoist\").shadowRoot.querySelector(\"#\\\\31 832fbe1d701120035ae23c7ce610369\")";
        WebElement clickUser1=(WebElement)jse.executeScript(clickUser);
        clickUser1.click();
        
        //Clicking on the Impersonate button
        Thread.sleep(3000);
        String clickIMPbutton="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button > slot > span\")";
        WebElement ipmButton=(WebElement)jse.executeScript(clickIMPbutton);
        ipmButton.click();
        System.out.println("Impersonated " + user);
        
        Thread.sleep(3000);
    }
	
	public void endImpersonation(JavascriptExecutor jse) throws InterruptedException {

        //End Impersonation
        driver.get(Config.baseUrl()); 
        Thread.sleep(3000);
        WebElement profile=(WebElement)jse.executeScript(profileIcon);
        profile.click();    
        
        Thread.sleep(3000);
        String endimp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.unimpersonate.keyboard-navigatable.polaris-enabled > div\")";
        WebElement endimpersonation=(WebElement)jse.executeScript(endimp);
        endimpersonation.click();
        Thread.sleep(3000);
    }
}