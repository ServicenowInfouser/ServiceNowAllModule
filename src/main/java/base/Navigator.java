package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Navigator extends BaseTest {
    
    private WebDriver driver;
    public WebDriverWait wait;
    private static JavascriptExecutor jse;

    public Navigator(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // ✅ Explicit wait
        this.jse = (JavascriptExecutor) driver;
    }

    // ✅ Helper method to execute JS and return WebElement safely
    public WebElement getElementByJs(String script) {
        return (WebElement) jse.executeScript(script);
    }

    // ✅ All menu navigation and opening given list
    public void allNavigation(String listName) {
        System.out.println("Navigating to All Menu...");

        // Click All Menu
        String allMenuScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
                + ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
                + ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
                + ".shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";

        WebElement allClick = getElementByJs(allMenuScript);
        wait.until(ExpectedConditions.elementToBeClickable(allClick)).click();

        System.out.println("Searching and opening list...");

        // Type in filter box
        String filterScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
                + ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
                + ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
                + ".shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\")"
                + ".shadowRoot.querySelector(\"#filter\")";

        WebElement filterBox = getElementByJs(filterScript);
        wait.until(ExpectedConditions.visibilityOf(filterBox)).sendKeys(listName, Keys.ENTER);

        System.out.println("Switching to iframe...");

        // Switch to iframe
        String iframeScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
                + ".shadowRoot.querySelector(\"#gsft_main\")";

        WebElement frame = getElementByJs(iframeScript);
        driver.switchTo().frame(frame);
    }

    // ✅ Click "New" button in UI
    public void newUIAction() {
        String newButtonScript = "return document.querySelector(\"#sysverb_new\")";
        WebElement newButton = getElementByJs(newButtonScript);
        wait.until(ExpectedConditions.elementToBeClickable(newButton)).click();
        System.out.println("Clicked New button.");
    }
    
    //Searching for the menu in all navigator
    public void searchMenu(String listName) {
        System.out.println("Navigating to All Menu...");

        // Click All Menu
        String allMenuScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
                + ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
                + ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
                + ".shadowRoot.querySelector(\"#d6e462a5c3533010cbd77096e940dd8c\")";

        WebElement allClick = getElementByJs(allMenuScript);
        wait.until(ExpectedConditions.elementToBeClickable(allClick)).click();

        System.out.println("Searching for menu...");

        // Type in filter box
        String filterScript = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
                + ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
                + ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
                + ".shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\")"
                + ".shadowRoot.querySelector(\"#filter\")";

        WebElement filterBox = getElementByJs(filterScript);
        wait.until(ExpectedConditions.visibilityOf(filterBox)).sendKeys(listName);

    }
    
    //All incident related operation
    public void createNewIncident() throws InterruptedException {
    	
    	searchMenu("Incident");
    	
    	Thread.sleep(3000);
    	//click on create new
        String createnew = "return document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
        		+ ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
        		+ ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
        		+ ".shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\")"
        		+ ".shadowRoot.querySelector(\"nav > div.sn-polaris-nav.d6e462a5c3533010cbd77096e940dd8c.can-animate > div.super-filter-container.all-results-open > div.all-results-section.section-open.results-section > div > div.sn-polaris-tab-content.-left.is-visible.can-animate > div > sn-collapsible-list:nth-child(3)\")"
        		+ ".shadowRoot.querySelector(\"#\\\\31 4641d70c611228501114133b3cc88a1 > span > span\")";
        WebElement clickcreatenew = getElementByJs(createnew);
        wait.until(ExpectedConditions.elementToBeClickable(clickcreatenew)).click();
    }
    
    
    //All Change related operation
    public void createNewChange() throws InterruptedException {
    	Thread.sleep(3000);
    	searchMenu("Change");
    	
    	Thread.sleep(3000);
    	//click on create new
        String createnew = "document.querySelector(\"body > macroponent-f51912f4c700201072b211d4d8c26010\")"
        		+ ".shadowRoot.querySelector(\"div > sn-canvas-appshell-root > sn-canvas-appshell-layout > sn-polaris-layout\")"
        		+ ".shadowRoot.querySelector(\"div.sn-polaris-layout.polaris-enabled > div.layout-main > div.header-bar > sn-polaris-header\")"
        		+ ".shadowRoot.querySelector(\"nav > div > div.starting-header-zone > sn-polaris-menu:nth-child(2)\")"
        		+ ".shadowRoot.querySelector(\"nav > div.sn-polaris-nav.d6e462a5c3533010cbd77096e940dd8c.can-animate > div.super-filter-container.all-results-open > div.all-results-section.section-open.results-section > div > div.sn-polaris-tab-content.-left.is-visible.can-animate > div > sn-collapsible-list:nth-child(1)\")"
        		+ ".shadowRoot.querySelector(\"#\\\\33 23bb07bc611227a018aea9eb8f3b35e > span\")";
        WebElement clickcreatenew = getElementByJs(createnew);
        clickcreatenew.click();
    }
    
}
