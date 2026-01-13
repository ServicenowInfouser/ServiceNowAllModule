package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;
    private final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    private final Duration DEFAULT_POLLING = Duration.ofMillis(500);

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

     //Specifically for Shadow DOM elements found via JavascriptExecutor
    public WebElement waitForShadowElement(String jsScript) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(DEFAULT_POLLING)
                .ignoring(Exception.class);

        return wait.until(d -> {
            WebElement element = (WebElement) ((JavascriptExecutor) d).executeScript(jsScript);
            if (element != null && element.isDisplayed()) {
                return element;
            }
            return null;	
        });
    }

    /**
     * Optional: General wait for standard elements (non-shadow)
     */
    public WebElement waitForElementVisible(org.openqa.selenium.By locator) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(DEFAULT_POLLING)
                .ignoring(org.openqa.selenium.NoSuchElementException.class);

        return wait.until(d -> d.findElement(locator));
    }
}