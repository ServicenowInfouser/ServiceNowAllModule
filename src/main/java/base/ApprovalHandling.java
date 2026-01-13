package base;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import utils.ExtentReportManager;
import utils.WaitUtils;

public class ApprovalHandling {

	private WebDriver driver;
	private WaitUtils waitUtils;

	public ApprovalHandling(WebDriver driver) {
		this.driver = driver;
		this.waitUtils = new WaitUtils(driver); // Initialize the utility
	}

	private void searchApproval(String recordNo, String approver) {
		driver.get(BaseTest.baseUrl + "/sysapproval_approver_list.do");

		// Dropdown
		WebElement approvalsDropdown = waitUtils
				.waitForElementVisible(By.xpath("//select[@class='form-control default-focus-outline']"));
		new Select(approvalsDropdown).selectByVisibleText("Approval for");

		// Global search
		WebElement globalSearchBox = waitUtils
				.waitForElementVisible(By.xpath("//input[@class='form-control' and @type='search']"));
		globalSearchBox.sendKeys(recordNo);
		globalSearchBox.sendKeys(Keys.ENTER);

		// Approver search
		WebElement approverSearch = waitUtils.waitForElementVisible(
				By.xpath("//*[@id='sysapproval_approver_table']/thead/tr[2]/td[4]/div/div/div/input"));
		approverSearch.sendKeys(approver);
		approverSearch.sendKeys(Keys.ENTER);

		// Click record
		WebElement requestedButton = waitUtils.waitForElementVisible(By.xpath("//*[@class='linked formlink']"));
		requestedButton.click();
	}

	public void approveApproval(String recordNo, String approver, ExtentTest test) {
		try {
			searchApproval(recordNo, approver);
			waitUtils.waitForElementVisible(By.id("approve")).click();

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Approval Approved", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (Exception e) {
			test.fail("Approval failed: " + e.getMessage());
		}
	}

	public void rejectApproval(String recordNo, String approver, ExtentTest test) {
		try {
			searchApproval(recordNo, approver);
			waitUtils.waitForElementVisible(By.id("reject")).click();

			String screenshotPath = ExtentReportManager.captureScreenshot_new(driver);
			test.info("Approval Rejected", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} catch (Exception e) {
			test.fail("Rejection failed: " + e.getMessage());
		}
	}
}