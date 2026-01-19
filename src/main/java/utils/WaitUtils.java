package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;
    private final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    // Specifically for Shadow DOM elements found via JavascriptExecutor
    public WebElement waitForShadowElement(String jsScript) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);

        return wait.until(d -> {
            WebElement element = (WebElement) ((JavascriptExecutor) d).executeScript(jsScript);
            return (element != null && element.isDisplayed()) ? element : null;
        });
    }

    // General wait for standard elements (non-shadow)
    public WebElement waitForElementVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}