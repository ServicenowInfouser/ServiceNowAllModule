package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Impersonation extends BaseTest {
	
	public static void startImpersonation(String user, WebDriver driver, JavascriptExecutor jse) throws InterruptedException {
		//Profile Icon click        
        driver.get(Config.baseUrl());               
        String profileIcon="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span > img\")";
        WebElement profile=(WebElement)jse.executeScript(profileIcon);
        profile.click();    
        
        //Impersonation Button click 
        String imp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.impersonateUser.keyboard-navigatable.polaris-enabled > div\")";
        WebElement profileImpersonation=(WebElement)jse.executeScript(imp);
        profileImpersonation.click();
        
        //Entering User name in search field
        Thread.sleep(2000);
        String searchUser="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal > div > now-typeahead\")";
        WebElement serchprofile=(WebElement)jse.executeScript(searchUser);
        serchprofile.sendKeys(user);
        
        //Selecting searched user
        Thread.sleep(2000);
        String clickUser="return document.querySelector(\"body > now-popover-panel > seismic-hoist\").shadowRoot.querySelector(\"#\\\\31 832fbe1d701120035ae23c7ce610369\")";
        WebElement clickUser1=(WebElement)jse.executeScript(clickUser);
        clickUser1.click();
        
        //Clicking on the Impersonate button
        Thread.sleep(2000);
        String clickIMPbutton="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.content-area.can-animate > sn-impersonation\").shadowRoot.querySelector(\"now-modal\").shadowRoot.querySelector(\"div > div > div > div.now-modal-footer > now-button:nth-child(2)\").shadowRoot.querySelector(\"button > slot > span\")";
        WebElement ipmButton=(WebElement)jse.executeScript(clickIMPbutton);
        ipmButton.click();
        
        Thread.sleep(2000);
    }
	
	public static void endImpersonation(WebDriver driver, JavascriptExecutor jse) throws InterruptedException {
		 
        //End Impersonation
        driver.get(Config.baseUrl()); 
        String profileIcon2="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"nav > div > div.ending-header-zone > div.polaris-header-controls > div.utility-menu-container > div > div > now-avatar\").shadowRoot.querySelector(\"span > span > img\")";
        WebElement profile2=(WebElement)jse.executeScript(profileIcon2);
        profile2.click();   
        
        Thread.sleep(2000);
        String endimp="return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\").shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\").shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\").shadowRoot.querySelector(\"#userMenu > span > span:nth-child(2) > div > div.user-menu-controls > button.user-menu-button.unimpersonate.keyboard-navigatable.polaris-enabled > div\")";
        WebElement endimpersonation=(WebElement)jse.executeScript(endimp);
        endimpersonation.click();
    }

}

//driver.get(baseUrl + "sysapproval_approver_list");
//Thread.sleep(2000);
//// Search Change record on table
//WebElement Approvals=driver.findElement(By.xpath("//select[@class=\"form-control default-focus-outline\"]"));
//Approvals.click();
//Thread.sleep(3000);
//
//Select selectValu=new Select(Approvals);
//selectValu.selectByVisibleText("Approval for");
//
//Thread.sleep(2000);
//WebElement globalSearchBox2 = driver.findElement(By.xpath("//input[@class='form-control' and @type='search']"));
//globalSearchBox2.sendKeys(value);
//Thread.sleep(2000);
//globalSearchBox2.sendKeys(Keys.ENTER);
//
//WebElement approversearch=driver.findElement(By.xpath("//*[@id=\"sysapproval_approver_table\"]/thead/tr[2]/td[4]/div/div/div/input"));
//approversearch.sendKeys("Manifah Masood");
//approversearch.sendKeys(Keys.ENTER);
//
//
//WebElement requestedbutton=driver.findElement(By.xpath("//*[@class='linked formlink']"));
//requestedbutton.click();
//
//driver.findElement(By.xpath("//*[@id=\"approve\"]")).click();
//Thread.sleep(2000);
