package UITestCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ChangePasswordTest extends BaseTest {
    WebDriver driver;



    @Test
    public void testChangePasswordSuccessfully() {
        // Precondition: Log in to the site
        driver.get("https://buggy.justtestit.org/");
        driver.findElement(By.xpath("/html/body/my-app/header/nav/div/my-login/div/form/div/input[1]")).sendKeys("kirusanthan"); // Replace with actual username
        driver.findElement(By.xpath("/html/body/my-app/header/nav/div/my-login/div/form/div/input[2]")).sendKeys("Malathi75@"); // Replace with actual password
        driver.findElement(By.xpath("/html/body/my-app/header/nav/div/my-login/div/form/button")).click();

        // Navigate to Change Password
        driver.findElement(By.xpath("//a[contains(text(), 'Change Password')]")).click();

        // Change Password
        driver.findElement(By.xpath("//input[@placeholder='Current Password']")).sendKeys("current_password"); // Replace with actual current password
        driver.findElement(By.xpath("//input[@placeholder='New Password']")).sendKeys("new_password"); // Replace with actual new password
        driver.findElement(By.xpath("//input[@placeholder='Confirm Password']")).sendKeys("new_password"); // Confirm new password
        driver.findElement(By.xpath("//button[contains(text(), 'Save')]")).click();

        // Verification: Check for success message
        String successMessage = driver.findElement(By.xpath("//div[contains(text(), 'Password changed successfully')]")).getText();
        Assert.assertTrue(successMessage.contains("Password changed successfully"), "Password change was not successful.");
    }

}