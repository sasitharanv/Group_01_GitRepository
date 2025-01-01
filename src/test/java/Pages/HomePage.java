package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage {
    WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickRegister() {
        driver.findElement(By.linkText("Register")).click();
    }

    public void clickLogin() {
        driver.findElement(By.linkText("Login")).click();
    }

    public void clickOverallRating() {
        driver.findElement(By.linkText("Overall Rating")).click();
    }

    public void clickProfile() {
        driver.findElement(By.linkText("Profile")).click();
    }

    public WebElement getFooterLink(String linkText) {
        return driver.findElement(By.linkText(linkText));
    }
}