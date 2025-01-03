package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    WebDriver driver;
    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickProfile() {

        driver.findElement(By.xpath("/html/body/my-app/header/nav/div/my-login/div/ul/li[2]/a")).click();
        String expectedUrl = "https://buggy.justtestit.org/profile";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds
        try {
            wait.until(ExpectedConditions.urlToBe(expectedUrl));

        } catch (TimeoutException e) {

            throw e; // Re-throw the exception to fail the test
        }

    }

    public void fillChangePasswordForm(String currentPassword,String newPassword,String confirmPassword) {
        By currentPasswordLocator = By.xpath("/html/body/my-app/div/main/my-profile/div/form/div[1]/div[3]/div/div/div/div[2]/fieldset[1]/input");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(currentPasswordLocator));
        driver.findElement(By.xpath("/html/body/my-app/div/main/my-profile/div/form/div[1]/div[3]/div/div/div/div[2]/fieldset[1]/input")).sendKeys(currentPassword);
        driver.findElement(By.id("newPassword")).sendKeys(newPassword);
        driver.findElement(By.id("newPasswordConfirmation")).sendKeys(confirmPassword);
        driver.findElement(By.xpath("/html/body/my-app/div/main/my-profile/div/form/div[2]/div/button")).click();

    }


}
