package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) throws InterruptedException {
        driver.get("https://dev311431.service-now.com");

        driver.findElement(By.xpath("//input[@id='user_name']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='user_password']")).sendKeys(password);
        driver.findElement(By.xpath("//button[@id='sysverb_login']")).click();

        Thread.sleep(5000); // wait for login to complete
        System.out.println("Login successful");
    }
}
